package es.ffgiraldez.comicsearch.query.sugestion.data

import arrow.core.Option
import arrow.core.toOption
import es.ffgiraldez.comicsearch.comics.data.ComicLocalDataSource
import es.ffgiraldez.comicsearch.comics.data.ComicRemoteDataSource
import es.ffgiraldez.comicsearch.comics.data.network.ComicVineApi
import es.ffgiraldez.comicsearch.comics.data.storage.ComicDatabase
import es.ffgiraldez.comicsearch.comics.domain.Query
import es.ffgiraldez.comicsearch.platform.ComicSchedulers
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class SuggestionRemoteDataSource(
        private val api: ComicVineApi
) : ComicRemoteDataSource<String> {
    override fun findByTerm(searchTerm: String): Single<List<String>> = api.fetchVolumes(searchTerm)
            .subscribeOn(ComicSchedulers.network)
            .map { response ->
                response.results
                        .distinctBy { it.name }
                        .map { it.name }
            }
}

class SuggestionLocalDataSource(
        private val database: ComicDatabase
) : ComicLocalDataSource<String> {
    override fun insert(query: String, titles: List<String>): Completable =
            Completable.fromAction {
                database.suggestionDao().insert(query, titles)
            }.subscribeOn(ComicSchedulers.database)

    override fun findQueryByTerm(searchTerm: String): Flowable<Option<Query>> = database.suggestionDao()
            .findQueryByTerm(searchTerm)
            .subscribeOn(ComicSchedulers.database)
            .flatMap { Flowable.just(it.firstOrNull().toOption()) }
            .map { search -> search.map { Query(it.queryId, it.searchTerm) } }

    override fun findByQuery(query: Query): Flowable<List<String>> = database.suggestionDao()
            .findSuggestionByQuery(query.identifier)
            .subscribeOn(ComicSchedulers.database)
            .map { suggestions -> suggestions.map { it.title } }
}