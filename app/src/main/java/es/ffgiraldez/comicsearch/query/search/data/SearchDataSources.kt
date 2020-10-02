package es.ffgiraldez.comicsearch.query.search.data

import es.ffgiraldez.comicsearch.comics.data.SuspendComicLocalDataSource
import es.ffgiraldez.comicsearch.comics.data.SuspendComicRemoteDataSource
import es.ffgiraldez.comicsearch.comics.data.network.SuspendComicVineApi
import es.ffgiraldez.comicsearch.comics.data.storage.ComicDatabase
import es.ffgiraldez.comicsearch.comics.domain.Query
import es.ffgiraldez.comicsearch.comics.domain.Volume
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SuspendSearchRemoteDataSource(
        private val api: SuspendComicVineApi
) : SuspendComicRemoteDataSource<Volume> {
    override suspend fun findByTerm(searchTerm: String): List<Volume> =
            api.fetchVolumes(searchTerm).run {
                results.filter { it.apiPublisher != null && it.apiImage != null }
                        .map { Volume(it.name, it.apiPublisher!!.name, it.apiImage!!.url) }

            }
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