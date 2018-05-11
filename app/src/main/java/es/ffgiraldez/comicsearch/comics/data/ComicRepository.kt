package es.ffgiraldez.comicsearch.comics.data

import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.comics.domain.ComicError.EmptyResultsError
import es.ffgiraldez.comicsearch.comics.domain.ComicError.NetworkError
import es.ffgiraldez.comicsearch.comics.domain.Query
import es.ffgiraldez.comicsearch.platform.left
import es.ffgiraldez.comicsearch.platform.right
import io.reactivex.Flowable

abstract class ComicRepository<T>(
        private val local: ComicLocalDataSource<T>,
        private val remote: ComicRemoteDataSource<T>
) {
    fun findByTerm(term: String): Flowable<Either<ComicError, List<T>>> =
            local.findQueryByTerm(term)
                    .flatMap { findSuggestions(it, term) }

    private fun findSuggestions(
            query: Option<Query>,
            term: String
    ): Flowable<out Either<ComicError, List<T>>> = when (query) {
        is None -> searchSuggestions(term)
        is Some -> fetchSuggestions(query)
    }

    private fun searchSuggestions(term: String): Flowable<Either<ComicError, List<T>>> =
            remote.findByTerm(term)
                    .map { right<ComicError, List<T>>(it) }
                    .onErrorReturn { left<ComicError, List<T>>(NetworkError) }
                    .flatMapPublisher { saveSuggestions(it, term) }

    private fun saveSuggestions(
            results: Either<ComicError, List<T>>,
            term: String
    ): Flowable<Either<ComicError, List<T>>> =
            results.fold({ _ ->
                Flowable.just(results)
            }, {
                local.insert(term, it).toFlowable<Either<ComicError, List<T>>>()
            })

    private fun fetchSuggestions(it: Some<Query>): Flowable<Either<EmptyResultsError, List<T>>> =
            local.findByQuery(it.t)
                    .map {
                        when (it.isEmpty()) {
                            true -> Either.left(EmptyResultsError)
                            false -> Either.right(it)
                        }
                    }
}