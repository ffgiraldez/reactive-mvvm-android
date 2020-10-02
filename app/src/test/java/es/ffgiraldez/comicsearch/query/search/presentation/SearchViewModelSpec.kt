package es.ffgiraldez.comicsearch.query.search.presentation

import androidx.lifecycle.asFlow
import app.cash.turbine.test
import arrow.core.Either
import com.nhaarman.mockitokotlin2.mock
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.comics.domain.Volume
import es.ffgiraldez.comicsearch.comics.gen.query
import es.ffgiraldez.comicsearch.comics.gen.search
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.base.presentation.toViewState
import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.checkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions
import org.mockito.ArgumentMatchers.anyString

class SearchViewModelSpec :
        StringSpec({
            "Search ViewModel should trigger search for a query" {
                checkAll(Arb.search(), Arb.query()) { results, query ->
                    runBlockingTest {
                        val viewModel = givenSuggestionViewModel(results)
                        val viewState = results.toViewState()
                        viewModel.state.asFlow().test {
                            Assertions.assertEquals(expectItem(), QueryViewState.idle<Volume>())
                            viewModel.inputQuery(query)
                            Assertions.assertEquals(expectItem(), QueryViewState.loading<Volume>())
                            Assertions.assertEquals(expectItem(), viewState)
                        }
                    }
                }
            }
        })

private fun SuspendSearchViewModel.inputQuery(input: String) {
    query.value = input
}

private fun givenSuggestionViewModel(results: Either<ComicError, List<Volume>>): SuspendSearchViewModel =
        SuspendSearchViewModel.invoke(mock {
            on { findByTerm(anyString()) }.thenReturn(flowOf(results))
        })
