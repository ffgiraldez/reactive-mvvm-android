package es.ffgiraldez.comicsearch.comics.gen

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.comics.domain.Volume
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*

fun Arb.Companion.suggestions(): Arb<Either<ComicError, List<String>>> = arb {
    generateSequence {
        when (Arb.bool().next(it)) {
            true -> Arb.comicError().next(it).left()
            false -> Arb.suggestionList().next(it).right()
        }
    }
}

fun Arb.Companion.suggestionList(): Arb<List<String>> = arb {
    generateSequence {
        Arb.query().values(it).take(10).map { it.value }.toList()
    }
}

fun Arb.Companion.suggestionsViewState(): Arb<QueryViewState<String>> = arb(
        listOf(QueryViewState.idle(), QueryViewState.loading())
) {
    generateSequence {
        Arb.suggestions().next(it).fold(
                { QueryViewState.error(it) },
                { QueryViewState.result(it) }
        )
    }
}

fun Arb.Companion.suggestionsErrorViewState(): Arb<QueryViewState.Error> = suggestionsViewState()
        .filter { it is QueryViewState.Error }
        .map { it as QueryViewState.Error }

fun Arb.Companion.suggestionsResultViewState(): Arb<QueryViewState.Result<String>> = suggestionsViewState()
        .filter { it is QueryViewState.Result<String> }
        .map { it as QueryViewState.Result<String> }

fun Arb.Companion.search(): Arb<Either<ComicError, List<Volume>>> = arb {
    generateSequence {
        when (Arb.bool().next(it)) {
            true -> Arb.comicError().next(it).left()
            false -> Arb.volumeList().next(it).right()
        }
    }
}

fun Arb.Companion.comicError(): Arb<ComicError> = arb { rs ->
    generateSequence {
        when (Arb.bool().next(rs)) {
            true -> ComicError.EmptyResultsError
            false -> ComicError.NetworkError
        }
    }
}

fun Arb.Companion.query(): Arb<String> = Arb.string(minSize = 1, maxSize = 10)

fun Arb.Companion.volume(): Arb<Volume> = Arb.bind(Arb.string(), Arb.string(), Arb.string(), ::Volume)

fun Arb.Companion.volumeList(): Arb<List<Volume>> = arb {
    generateSequence {
        Arb.volume().values(it).take(10).map { it.value }.toList()
    }
}

fun Arb.Companion.searchViewState(): Arb<QueryViewState<Volume>> = arb(
        listOf(QueryViewState.idle(), QueryViewState.loading())
) {
    generateSequence {
        Arb.search().next(it).fold(
                { QueryViewState.error(it) },
                { QueryViewState.result(it) }
        )
    }
}

fun Arb.Companion.searchErrorViewState(): Arb<QueryViewState.Error> = searchViewState()
        .filter { it is QueryViewState.Error }
        .map { it as QueryViewState.Error }

fun Arb.Companion.searchResultViewState(): Arb<QueryViewState.Result<Volume>> = searchViewState()
        .filter { it is QueryViewState.Result<Volume> }
        .map { it as QueryViewState.Result<Volume> }