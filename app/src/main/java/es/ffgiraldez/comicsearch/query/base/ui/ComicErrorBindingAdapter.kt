package es.ffgiraldez.comicsearch.query.base.ui

import es.ffgiraldez.comicsearch.comics.domain.ComicError

fun ComicError.toHumanResponse(): String = when (this) {
    ComicError.NetworkError -> "no internet connection"
    ComicError.EmptyResultsError -> "search without suggestion"
}