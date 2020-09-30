package es.ffgiraldez.comicsearch.query.sugestion.presentation

import arrow.core.Either
import com.nhaarman.mockitokotlin2.mock
import es.ffgiraldez.comicsearch.comic.gen.query
import es.ffgiraldez.comicsearch.comic.gen.suggestions
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.platform.toFlowable
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.base.presentation.toViewState
import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.checkAll

import io.kotlintest.provided.ProjectConfig

import io.reactivex.Flowable
import org.mockito.ArgumentMatchers.anyString
import java.util.concurrent.TimeUnit.SECONDS

class SuggestionViewModelSpec :
        StringSpec({
            "Suggestion ViewModel should not trigger search for empty query" {
                checkAll(Arb.suggestions()) { suggestions ->
                    val viewModel = givenSuggestionViewModel(suggestions)
                    val observer = viewModel.state.toFlowable().test()

                    viewModel.inputQuery("")

                    observer.assertNotComplete()
                            .assertNoErrors()
                            .assertValues(QueryViewState.idle())
                }
            }

            "Suggestion ViewModel should trigger search for a valid query" {
                checkAll(Arb.suggestions(), Arb.query()) { suggestions, query ->
                    val viewModel = givenSuggestionViewModel(suggestions)
                    val observer = viewModel.state.toFlowable().test()
                    val viewState = suggestions.toViewState()

                    viewModel.inputQuery(query)

                    observer.assertNotComplete()
                            .assertNoErrors()
                            .assertValues(QueryViewState.idle(), QueryViewState.loading(), viewState)
                }
            }


        })

private fun SuggestionViewModel.inputQuery(input: String) {
    query.value = input
    ProjectConfig.testScheduler.advanceTimeBy(10, SECONDS)
}

private fun givenSuggestionViewModel(suggestions: Either<ComicError, List<String>>): SuggestionViewModel =
        SuggestionViewModel.invoke(mock {
            on { findByTerm(anyString()) }.thenReturn(Flowable.just(suggestions))
        })
