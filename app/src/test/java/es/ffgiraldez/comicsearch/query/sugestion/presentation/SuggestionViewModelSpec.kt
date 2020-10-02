package es.ffgiraldez.comicsearch.query.sugestion.presentation

import androidx.lifecycle.asFlow
import app.cash.turbine.test
import arrow.core.Either
import com.nhaarman.mockitokotlin2.mock
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.comics.gen.query
import es.ffgiraldez.comicsearch.comics.gen.suggestions
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.base.presentation.toViewState
import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.checkAll
import io.kotlintest.provided.ProjectConfig
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.ArgumentMatchers.anyString
import java.util.concurrent.TimeUnit.SECONDS

class SuggestionViewModelSpec :
        StringSpec({
            "Suggestion ViewModel should not trigger search for empty query" {
                checkAll(Arb.suggestions()) { suggestions ->
                    runBlockingTest {
                        val viewModel = givenSuggestionViewModel(suggestions)
                        viewModel.state.asFlow().test {
                            assertEquals(expectItem(), QueryViewState.idle<String>())
                            viewModel.inputQuery("")
                        }
                    }
                }
            }

            "Suggestion ViewModel should trigger search for a valid query" {
                checkAll(Arb.suggestions(), Arb.query()) { suggestions, query ->
                    runBlockingTest {
                        val viewModel = givenSuggestionViewModel(suggestions)
                        val viewState = suggestions.toViewState()

                        viewModel.state.asFlow().test {
                            assertEquals(expectItem(), QueryViewState.idle<String>())
                            viewModel.inputQuery(query)
                            assertEquals(expectItem(), QueryViewState.loading<String>())
                            assertEquals(expectItem(), viewState)
                        }
                    }
                }
            }


        })

private fun SuspendSuggestionViewModel.inputQuery(input: String) {
    query.value = input
    ProjectConfig.testDispatcher.advanceTimeBy(SECONDS.toMillis(10))
}

private fun givenSuggestionViewModel(suggestions: Either<ComicError, List<String>>): SuspendSuggestionViewModel =
        SuspendSuggestionViewModel.invoke(mock {
            on { findByTerm(anyString()) }.thenReturn(flowOf(suggestions))
        })
