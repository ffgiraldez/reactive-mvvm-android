package es.ffgiraldez.comicsearch.query.base.ui

import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState

val <T>QueryViewState<T>.error: ComicError?
    get() = when (this) {
        is QueryViewState.Error -> _error
        else -> null
    }

val <T>QueryViewState<T>.results: List<T>
    get() = when (this) {
        is QueryViewState.Result -> _results
        else -> emptyList()
    }

val <T>QueryViewState<T>.loading: Boolean
    get() = when (this) {
        is QueryViewState.Loading -> true
        else -> false
    }