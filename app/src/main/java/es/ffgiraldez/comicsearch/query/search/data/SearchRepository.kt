package es.ffgiraldez.comicsearch.query.search.data

import es.ffgiraldez.comicsearch.comics.data.SuspendComicRepository
import es.ffgiraldez.comicsearch.comics.domain.Volume

class SuspendSearchRepository(
        local: SuspendSearchLocalDataSource,
        remote: SuspendSearchRemoteDataSource
) : SuspendComicRepository<Volume>(local, remote)
