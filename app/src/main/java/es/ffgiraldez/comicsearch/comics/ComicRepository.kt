package es.ffgiraldez.comicsearch.comics

import es.ffgiraldez.comicsearch.comics.data.ComicVineApi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ComicRepository(
        private val api: ComicVineApi
) {
    fun searchSuggestion(query: String): Single<List<String>> = api.fetchSuggestedVolumes(query)
            .subscribeOn(Schedulers.io())
            .map { response ->
                response.results
                        .distinctBy { it.name }
                        .map { it.name }
            }
            .subscribeOn(Schedulers.computation())

    fun searchVolume(query: String): Single<List<Volume>> = api.fetchVolumes(query)
            .subscribeOn(Schedulers.io())
            .map { response ->
                response.results
                        .filter { it.apiPublisher != null && it.apiImage != null }
                        .map {
                            Volume(it.name, it.apiPublisher!!.name, it.apiImage!!.url)
                        }
            }
            .subscribeOn(Schedulers.computation())
}