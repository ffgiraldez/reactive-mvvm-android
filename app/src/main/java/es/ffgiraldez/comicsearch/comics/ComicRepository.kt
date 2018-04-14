package es.ffgiraldez.comicsearch.comics

import android.util.Log
import es.ffgiraldez.comicsearch.comics.data.ComicVineApi
import es.ffgiraldez.comicsearch.comics.store.ComicDatabase
import es.ffgiraldez.comicsearch.comics.store.QueryEntity
import es.ffgiraldez.comicsearch.comics.store.SearchEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class ComicRepository(
        private val api: ComicVineApi,
        private val database: ComicDatabase
) {

    fun findSuggestion(term: String): Flowable<List<String>> =
            database.suggestionDao().findQueryByTerm(term)
                    .flatMap {
                        when (it.isEmpty()) {
                            true -> searchSuggestionsOnApi(term)
                            false -> searchSuggestionsOnDatabase(it.first())
                        }
                    }

    fun findVolume(term: String): Flowable<List<Volume>> =
            database.volumeDao().findQueryByTerm(term)
                    .flatMap {
                        when (it.isEmpty()) {
                            true -> searchVolumeOnApi(term)
                            false -> searchVolumeOnDatabase(it.first())
                        }
                    }

    private fun searchSuggestionsOnApi(term: String): Flowable<List<String>> =
            api.fetchSuggestedVolumes(term)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { Log.d("cambio", "pidiendo findSuggestion la api") }
                    .map { response ->
                        response.results
                                .distinctBy { it.name }
                                .map { it.name }
                    }
                    .flatMapCompletable { response ->
                        Completable.fromAction {
                            Log.d("cambio", "guardando query: $term en bd")
                            database.suggestionDao().insert(
                                    term,
                                    response
                            )
                        }
                    }.toFlowable<List<String>>()

    private fun searchSuggestionsOnDatabase(query: QueryEntity): Flowable<List<String>> =
            database.suggestionDao().findSuggestionByQuery(query.queryId)
                    .doOnSubscribe { Log.d("cambio", "devolviendo resultados") }
                    .subscribeOn(Schedulers.io())
                    .map { suggestions -> suggestions.map { it.title } }

    private fun searchVolumeOnApi(term: String): Flowable<List<Volume>> =
            api.fetchVolumes(term)
                    .subscribeOn(Schedulers.io())
                    .map { response ->
                        response.results
                                .filter { it.apiPublisher != null && it.apiImage != null }
                                .map {
                                    Volume(it.name, it.apiPublisher!!.name, it.apiImage!!.url)
                                }
                    }
                    .subscribeOn(Schedulers.computation())
                    .flatMapCompletable { response ->
                        Completable.fromAction {
                            Log.d("cambio", "guardando query: $term en bd")
                            database.volumeDao().insert(
                                    term,
                                    response
                            )
                        }
                    }.toFlowable<List<Volume>>()

    private fun searchVolumeOnDatabase(query: SearchEntity): Flowable<List<Volume>> =
            database.volumeDao().findVolumeByQuery(query.queryId)
                    .doOnSubscribe { Log.d("cambio", "devolviendo resultados") }
                    .subscribeOn(Schedulers.io())
                    .map { volumeList -> volumeList.map { Volume(it.title, it.author, it.url) } }
}