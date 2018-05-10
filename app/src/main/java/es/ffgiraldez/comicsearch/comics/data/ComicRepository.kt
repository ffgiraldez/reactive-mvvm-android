package es.ffgiraldez.comicsearch.comics.data

import arrow.core.Either
import arrow.core.None
import arrow.core.Some
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.comics.domain.ComicError.EmptyResultsError
import es.ffgiraldez.comicsearch.comics.domain.ComicError.NetworkError
import es.ffgiraldez.comicsearch.platform.left
import es.ffgiraldez.comicsearch.platform.right
import io.reactivex.Flowable

abstract class ComicRepository<T>(
        private val local: ComicLocalDataSource<T>,
        private val remote: ComicRemoteDataSource<T>
) {
    fun findByTerm(term: String): Flowable<Either<ComicError, List<T>>> =
            local.findQueryByTerm(term)
                    .flatMap {
                        when (it) {
                            is None -> remote.findByTerm(term)
                                    .map { right<ComicError, List<T>>(it) }
                                    .onErrorReturn { left<ComicError, List<T>>(NetworkError) }
                                    .toFlowable()
                            is Some -> local.findByQuery(it.t)
                                    .map {
                                        when (it.isEmpty()) {
                                            true -> Either.left(EmptyResultsError)
                                            false -> Either.right(it)
                                        }
                                    }
                        }
                    }
}