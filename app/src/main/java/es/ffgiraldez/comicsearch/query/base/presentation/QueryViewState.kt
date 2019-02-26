package es.ffgiraldez.comicsearch.query.base.presentation

import arrow.core.Either
import es.ffgiraldez.comicsearch.comics.domain.ComicError

sealed class QueryViewState<out T> {

    companion object {
        fun <T> result(results: List<T>): QueryViewState<T> = Result(results)
        fun <T> idle(): QueryViewState<T> = Idle
        fun <T> loading(): QueryViewState<T> = Loading
        fun <T> error(error: ComicError): QueryViewState<T> = Error(error)
    }

    object Idle : QueryViewState<Nothing>()
    object Loading : QueryViewState<Nothing>()
    data class Result<out T>(val _results: List<T>) : QueryViewState<T>()
    data class Error(val _error: ComicError) : QueryViewState<Nothing>()

}

fun <T> Either<ComicError, List<T>>.toViewState(): QueryViewState<T> = fold(
        { QueryViewState.error(it) },
        { QueryViewState.result(it) }
)
