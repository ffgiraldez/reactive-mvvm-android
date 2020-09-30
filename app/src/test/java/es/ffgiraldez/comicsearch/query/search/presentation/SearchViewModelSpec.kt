package es.ffgiraldez.comicsearch.query.search.presentation

import arrow.core.Either
import com.nhaarman.mockitokotlin2.mock
import es.ffgiraldez.comicsearch.comic.gen.query
import es.ffgiraldez.comicsearch.comic.gen.search
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.comics.domain.Volume
import es.ffgiraldez.comicsearch.platform.toFlowable
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.base.presentation.toViewState
import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.checkAll

import io.reactivex.Flowable
import org.mockito.ArgumentMatchers.anyString

class SearchViewModelSpec :
        StringSpec({
            "Search ViewModel should trigger search for a query" {
                checkAll(Arb.search(), Arb.query()) { results, query ->
                    val viewModel = givenSuggestionViewModel(results)
                    val observer = viewModel.state.toFlowable().test()
                    val viewState = results.toViewState()

                    viewModel.inputQuery(query)

                    observer.assertNotComplete()
                            .assertNoErrors()
                            .assertValues(QueryViewState.idle(), QueryViewState.loading(), viewState)
                }
            }


        })

private fun SearchViewModel.inputQuery(input: String) {
    query.value = input
}

private fun givenSuggestionViewModel(results: Either<ComicError, List<Volume>>): SearchViewModel =
        SearchViewModel.invoke(mock {
            on { findByTerm(anyString()) }.thenReturn(Flowable.just(results))
        })
