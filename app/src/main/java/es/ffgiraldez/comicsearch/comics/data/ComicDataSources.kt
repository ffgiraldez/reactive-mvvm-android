package es.ffgiraldez.comicsearch.comics.data

import es.ffgiraldez.comicsearch.comics.domain.Query
import kotlinx.coroutines.flow.Flow

interface SuspendComicLocalDataSource<T> {
    fun findQueryByTerm(searchTerm: String): Flow<Query?>
    fun findByQuery(query: Query): Flow<List<T>>
    suspend fun insert(query: String, titles: List<T>)
}

interface SuspendComicRemoteDataSource<T> {
    suspend fun findByTerm(searchTerm: String): List<T>
}