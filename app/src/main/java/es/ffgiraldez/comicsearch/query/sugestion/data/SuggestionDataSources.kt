package es.ffgiraldez.comicsearch.query.sugestion.data

import es.ffgiraldez.comicsearch.comics.data.SuspendComicLocalDataSource
import es.ffgiraldez.comicsearch.comics.data.SuspendComicRemoteDataSource
import es.ffgiraldez.comicsearch.comics.data.network.SuspendComicVineApi
import es.ffgiraldez.comicsearch.comics.data.storage.ComicDatabase
import es.ffgiraldez.comicsearch.comics.domain.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SuspendSuggestionRemoteDataSource(
        private val api: SuspendComicVineApi
) : SuspendComicRemoteDataSource<String> {
    override suspend fun findByTerm(searchTerm: String): List<String> = api.fetchSuggestedVolumes(searchTerm)
            .let { response ->
                response.results
                        .distinctBy { it.name }
                        .map { it.name }
            }
}

class SuspendSuggestionLocalDataSource(
        private val database: ComicDatabase
) : SuspendComicLocalDataSource<String> {
    override suspend fun insert(query: String, titles: List<String>) =
            database.suspendingSuggestionDao().insert(query, titles)

    override fun findQueryByTerm(searchTerm: String): Flow<Query?> =
            database.suspendingSuggestionDao()
                    .findQueryByTerm(searchTerm)
                    .map { results -> results?.let { Query(it.queryId, it.searchTerm) } }

    override fun findByQuery(query: Query): Flow<List<String>> =
            database.suspendingSuggestionDao()
                    .findSuggestionByQuery(query.identifier)
                    .map { suggestions -> suggestions.map { it.title } }
}