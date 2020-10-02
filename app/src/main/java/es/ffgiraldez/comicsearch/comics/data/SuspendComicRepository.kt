package es.ffgiraldez.comicsearch.comics.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.comics.domain.Query
import kotlinx.coroutines.flow.*

abstract class SuspendComicRepository<T>(
        private val local: SuspendComicLocalDataSource<T>,
        private val remote: SuspendComicRemoteDataSource<T>
) {
    fun findByTerm(term: String): Flow<Either<ComicError, List<T>>> =
            local.findQueryByTerm(term)
                    .flatMapConcat { query -> find(query, term) }

    private fun find(query: Query?, term: String): Flow<Either<ComicError, List<T>>> =
            query?.run { fetch(this) } ?: search(term)

    private fun search(term: String): Flow<Either<ComicError, List<T>>> = flow {
        emit(
                Either.catch {
                    remote.findByTerm(term)
                }.mapLeft { ComicError.network() }
        )
    }.flatMapConcat { results -> save(results, term) }

    private fun save(
            results: Either<ComicError, List<T>>,
            term: String
    ): Flow<Either<ComicError, List<T>>> =
            results.fold(
                    { flowOf(results) },
                    {
                        flow {
                            local.insert(term, it)
                        }
                    }
            )

    private fun fetch(query: Query): Flow<Either<ComicError, List<T>>> =
            local.findByQuery(query)
                    .map {
                        when {
                            it.isEmpty() -> ComicError.emptyResult().left()
                            else -> it.right()
                        }
                    }
}