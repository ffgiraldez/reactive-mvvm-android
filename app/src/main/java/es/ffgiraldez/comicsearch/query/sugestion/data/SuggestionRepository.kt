package es.ffgiraldez.comicsearch.query.sugestion.data

import es.ffgiraldez.comicsearch.comics.data.ComicRepositoryEither

class SuggestionRepository(
        local: SuggestionLocalDataSource,
        remote: SuggestionRemoteDataSource
) : ComicRepositoryEither<String>(local, remote)
