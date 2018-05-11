package es.ffgiraldez.comicsearch.query.sugestion.data

import es.ffgiraldez.comicsearch.comics.data.ComicRepository

class SuggestionRepository(
        local: SuggestionLocalDataSource,
        remote: SuggestionRemoteDataSource
) : ComicRepository<String>(local, remote)
