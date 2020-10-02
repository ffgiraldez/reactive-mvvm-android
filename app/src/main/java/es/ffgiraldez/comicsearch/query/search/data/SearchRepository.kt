package es.ffgiraldez.comicsearch.query.search.data

import es.ffgiraldez.comicsearch.comics.data.ComicRepository
import es.ffgiraldez.comicsearch.comics.data.SuspendComicRepository
import es.ffgiraldez.comicsearch.comics.domain.Volume

class SearchRepository(
        local: SearchLocalDataSource,
        remote: SearchRemoteDataSource
) : ComicRepository<Volume>(local, remote)

class SuspendSearchRepository(
        local: SuspendSearchLocalDataSource,
        remote: SuspendSearchRemoteDataSource
) : SuspendComicRepository<Volume>(local, remote)
