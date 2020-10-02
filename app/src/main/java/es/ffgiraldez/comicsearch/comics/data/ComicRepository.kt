package es.ffgiraldez.comicsearch.comics.data

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.comics.domain.ComicError.EmptyResultsError
import es.ffgiraldez.comicsearch.comics.domain.ComicError.NetworkError
import es.ffgiraldez.comicsearch.comics.domain.Query
import es.ffgiraldez.comicsearch.platform.Option
import es.ffgiraldez.comicsearch.platform.left
import es.ffgiraldez.comicsearch.platform.right
import io.reactivex.Flowable

abstract class ComicRepository<T>(
        private val local: ComicLocalDataSource<T>,
        private val remote: ComicRemoteDataSource<T>
) {
    fun findByTerm(term: String): Flowable<Either<ComicError, List<T>>> =
            local.findQueryByTerm(term)
                    .flatMap { find(it, term) }

    private fun find(
            query: Option<Query>,
            term: String
    ): Flowable<out Either<ComicError, List<T>>> = when (query) {
        is Left -> search(term)
        is Right -> fetch(query)
    }

    private fun search(term: String): Flowable<Either<ComicError, List<T>>> =
            remote.findByTerm(term)
                    .map { right<ComicError, List<T>>(it) }
                    .onErrorReturn { left<ComicError, List<T>>(NetworkError) }
                    .flatMapPublisher { save(it, term) }

    private fun save(
            results: Either<ComicError, List<T>>,
            term: String
    ): Flowable<Either<ComicError, List<T>>> =
            results.fold({
                Flowable.just(results)
            }, {
                local.insert(term, it).toFlowable()
            })

    private fun fetch(it: Right<Query>): Flowable<Either<EmptyResultsError, List<T>>> =
            local.findByQuery(it.b)
                    .map {
                        when (it.isEmpty()) {
                            true -> Either.left(EmptyResultsError)
                            false -> Either.right(it)
                        }
                    }
}