package es.ffgiraldez.comicsearch.query.base.presentation

import es.ffgiraldez.comicsearch.comics.domain.ComicError

sealed class QueryViewState<out T> {

    companion object {
        fun <T> result(volumeList: List<T>): QueryViewState<T> = Result(volumeList)
        fun <T> idle(): QueryViewState<T> = Idle
        fun <T> loading(): QueryViewState<T> = Loading
        fun <T> error(error: ComicError): QueryViewState<T> = Error(error)
    }

    object Idle : QueryViewState<Nothing>()
    object Loading : QueryViewState<Nothing>()
    data class Result<out T>(val _results: List<T>) : QueryViewState<T>()
    data class Error(val _error: ComicError) : QueryViewState<Nothing>()

}

