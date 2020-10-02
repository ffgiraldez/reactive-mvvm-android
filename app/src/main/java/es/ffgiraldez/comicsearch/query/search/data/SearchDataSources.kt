package es.ffgiraldez.comicsearch.query.search.data

import es.ffgiraldez.comicsearch.comics.data.ComicLocalDataSource
import es.ffgiraldez.comicsearch.comics.data.ComicRemoteDataSource
import es.ffgiraldez.comicsearch.comics.data.SuspendComicLocalDataSource
import es.ffgiraldez.comicsearch.comics.data.SuspendComicRemoteDataSource
import es.ffgiraldez.comicsearch.comics.data.network.ComicVineApi
import es.ffgiraldez.comicsearch.comics.data.network.SuspendComicVineApi
import es.ffgiraldez.comicsearch.comics.data.storage.ComicDatabase
import es.ffgiraldez.comicsearch.comics.domain.Query
import es.ffgiraldez.comicsearch.comics.domain.Volume
import es.ffgiraldez.comicsearch.platform.ComicSchedulers
import es.ffgiraldez.comicsearch.platform.Option
import es.ffgiraldez.comicsearch.platform.toOption
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchRemoteDataSource(
        private val api: ComicVineApi
) : ComicRemoteDataSource<Volume> {
    override fun findByTerm(searchTerm: String): Single<List<Volume>> = api.fetchVolumes(searchTerm)
            .subscribeOn(ComicSchedulers.network)
            .map { response ->
                response.results
                        .filter { it.apiPublisher != null && it.apiImage != null }
                        .map {
                            Volume(it.name, it.apiPublisher!!.name, it.apiImage!!.url)
                        }
            }
}

class SuspendSearchRemoteDataSource(
        private val api: SuspendComicVineApi
) : SuspendComicRemoteDataSource<Volume> {
    override suspend fun findByTerm(searchTerm: String): List<Volume> =
            api.fetchVolumes(searchTerm).run {
                results.filter { it.apiPublisher != null && it.apiImage != null }
                        .map { Volume(it.name, it.apiPublisher!!.name, it.apiImage!!.url) }

            }
}

class SearchLocalDataSource(
        private val database: ComicDatabase
) : ComicLocalDataSource<Volume> {
    override fun insert(query: String, titles: List<Volume>): Completable =
            Completable.fromAction {
                database.volumeDao().insert(query, titles)
            }.subscribeOn(ComicSchedulers.database)

    override fun findQueryByTerm(searchTerm: String): Flowable<Option<Query>> = database.volumeDao()
            .findQueryByTerm(searchTerm)
            .subscribeOn(ComicSchedulers.database)
            .flatMap { Flowable.just(it.firstOrNull().toOption()) }
            .map { search -> search.map { Query(it.queryId, it.searchTerm) } }

    override fun findByQuery(query: Query): Flowable<List<Volume>> = database.volumeDao()
            .findVolumeByQuery(query.identifier)
            .subscribeOn(ComicSchedulers.database)
            .map { volumeList -> volumeList.map { Volume(it.title, it.author, it.url) } }
}

class SuspendSearchLocalDataSource(
        private val database: ComicDatabase
) : SuspendComicLocalDataSource<Volume> {
    override suspend fun insert(query: String, titles: List<Volume>) =
            database.suspendingVolumeDao().insert(query, titles)

    override fun findQueryByTerm(searchTerm: String): Flow<Query?> =
            database.suspendingVolumeDao().findQueryByTerm(searchTerm)
                    .map { queries -> queries?.let { Query(it.queryId, it.searchTerm) } }

    override fun findByQuery(query: Query): Flow<List<Volume>> =
            database.suspendingVolumeDao().findVolumeByQuery(query.identifier)
                    .map { results -> results.map { Volume(it.title, it.author, it.url) } }
}