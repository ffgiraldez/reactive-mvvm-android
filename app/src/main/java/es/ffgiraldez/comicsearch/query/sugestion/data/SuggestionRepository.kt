package es.ffgiraldez.comicsearch.query.sugestion.data

import es.ffgiraldez.comicsearch.comics.data.ComicRepository
import es.ffgiraldez.comicsearch.comics.data.SuspendComicRepository

class SuggestionRepository(
        local: SuggestionLocalDataSource,
        remote: SuggestionRemoteDataSource
) : ComicRepository<String>(local, remote)

class SuspendSuggestionRepository(
        local: SuspendSuggestionLocalDataSource,
        remote: SuspendSuggestionRemoteDataSource
) : SuspendComicRepository<String>(local, remote)
