package es.ffgiraldez.comicsearch.comics.data

import es.ffgiraldez.comicsearch.comics.domain.Query
import es.ffgiraldez.comicsearch.platform.Option
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

interface ComicLocalDataSource<T> {
    fun findQueryByTerm(searchTerm: String): Flowable<Option<Query>>
    fun findByQuery(query: Query): Flowable<List<T>>
    fun insert(query: String, titles: List<T>): Completable
}

interface SuspendComicLocalDataSource<T> {
    fun findQueryByTerm(searchTerm: String): Flow<Query?>
    fun findByQuery(query: Query): Flow<List<T>>
    suspend fun insert(query: String, titles: List<T>)
}

interface ComicRemoteDataSource<T> {
    fun findByTerm(searchTerm: String): Single<List<T>>
}

interface SuspendComicRemoteDataSource<T> {
    suspend fun findByTerm(searchTerm: String): List<T>
}