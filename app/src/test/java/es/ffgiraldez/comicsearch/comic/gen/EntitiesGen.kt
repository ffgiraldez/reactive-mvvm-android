package es.ffgiraldez.comicsearch.comic.gen

import arrow.core.Either
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.comics.domain.Volume
import es.ffgiraldez.comicsearch.platform.left
import es.ffgiraldez.comicsearch.platform.right
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import io.kotlintest.properties.Gen
import io.kotlintest.properties.filterIsInstance

class ComicErrorGenerator : Gen<ComicError> {
    override fun constants(): Iterable<ComicError> = emptyList()

    override fun random(): Sequence<ComicError> = generateSequence {
        takeIf { Gen.bool().random().first() }
                ?.let { ComicError.EmptyResultsError } ?: ComicError.NetworkError
    }
}

class QueryGenerator : Gen<String> {
    override fun constants(): Iterable<String> = Gen.string().constants().filter { it.isNotEmpty() }

    override fun random(): Sequence<String> = Gen.string().random().filter { it.isNotEmpty() }
}

class SuggestionGenerator : Gen<Either<ComicError, List<String>>> {
    override fun constants(): Iterable<Either<ComicError, List<String>>> = emptyList()

    override fun random(): Sequence<Either<ComicError, List<String>>> = generateSequence {
        generateEither(Gen.bool().random().first())
    }

    private fun generateEither(it: Boolean): Either<ComicError, List<String>> {
        return if (it) {
            left(Gen.comicError().random().first())
        } else {
            right((1..10).fold(emptyList()) { acc, _ -> acc + Gen.query().random().iterator().next() })
        }
    }
}

class VolumeGenerator : Gen<Volume> {
    override fun constants(): Iterable<Volume> = emptyList()

    override fun random(): Sequence<Volume> = generateSequence {
        Volume(
                Gen.string().random().first(),
                Gen.string().random().first(),
                Gen.string().random().first()
        )
    }

}

class SearchGenerator : Gen<Either<ComicError, List<Volume>>> {
    override fun constants(): Iterable<Either<ComicError, List<Volume>>> = emptyList()

    override fun random(): Sequence<Either<ComicError, List<Volume>>> = generateSequence {
        generateEither(Gen.bool().random().first())
    }

    private fun generateEither(it: Boolean): Either<ComicError, List<Volume>> {
        return if (it) {
            left(Gen.comicError().random().first())
        } else {
            right((1..10).fold(emptyList()) { acc, _ -> acc + Gen.volume().random().iterator().next() })
        }
    }
}

fun Gen.Companion.suggestions(): Gen<Either<ComicError, List<String>>> = SuggestionGenerator()

fun Gen.Companion.suggestionsViewState(): Gen<QueryViewState<String>> = Gen.suggestions().map { suggestion ->
    suggestion.fold(
            { QueryViewState.error<String>(it) },
            { QueryViewState.result(it) }
    )
}

fun Gen.Companion.suggestionsErrorViewState(): Gen<QueryViewState.Error> = suggestionsViewState().filterIsInstance()

fun Gen.Companion.suggestionsResultViewState(): Gen<QueryViewState.Result<String>> = suggestionsViewState().filterIsInstance()

fun Gen.Companion.search(): Gen<Either<ComicError, List<Volume>>> = SearchGenerator()

fun Gen.Companion.comicError(): Gen<ComicError> = ComicErrorGenerator()

fun Gen.Companion.query(): Gen<String> = QueryGenerator()

fun Gen.Companion.volume(): Gen<Volume> = VolumeGenerator()
