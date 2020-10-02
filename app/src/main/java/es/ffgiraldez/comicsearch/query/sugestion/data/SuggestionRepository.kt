package es.ffgiraldez.comicsearch.query.sugestion.data

import es.ffgiraldez.comicsearch.comics.data.SuspendComicRepository

class SuspendSuggestionRepository(
        local: SuspendSuggestionLocalDataSource,
        remote: SuspendSuggestionRemoteDataSource
) : SuspendComicRepository<String>(local, remote)
