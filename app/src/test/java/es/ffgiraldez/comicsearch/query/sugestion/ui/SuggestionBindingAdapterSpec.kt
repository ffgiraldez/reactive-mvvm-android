package es.ffgiraldez.comicsearch.query.sugestion.ui

import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import es.ffgiraldez.comicsearch.comic.gen.suggestionsErrorViewState
import es.ffgiraldez.comicsearch.comic.gen.suggestionsResultViewState
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.base.ui.toHumanResponse
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.specs.StringSpec
import junit.framework.Assert.assertEquals


class SuggestionBindingAdapterSpec :
        StringSpec({
            "FloatingSearchView should show progress on loading state" {
                val searchView = mock<FloatingSearchView>()

                searchView.bindSuggestions(QueryViewState.loading())

                verify(searchView).showProgress()

            }

            "FloatingSearchView should hide progress on non loading state" {
                val searchView = mock<FloatingSearchView>()

                searchView.bindSuggestions(QueryViewState.idle())

                verify(searchView).hideProgress()

            }

            "FloatingSearchView should not have interaction on null state" {
                val searchView = mock<FloatingSearchView>()

                searchView.bindSuggestions(null)

                verifyZeroInteractions(searchView)

            }

            "FloatingSearchView should show human description on error state" {
                assertAll(Gen.suggestionsErrorViewState()) { state ->
                    val captor = argumentCaptor<List<SearchSuggestion>>()
                    val searchView = mock<FloatingSearchView> {
                        doNothing().whenever(it).swapSuggestions(captor.capture())
                    }

                    searchView.bindSuggestions(state)

                    verify(searchView).swapSuggestions(any())

                    with(captor.firstValue) {
                        assertEquals(1, size)
                        assertEquals(state._error.toHumanResponse(), get(0).body)
                    }
                }
            }

            "FloatingSearchView should show results on result state" {
                assertAll(Gen.suggestionsResultViewState()) { state ->
                    val captor = argumentCaptor<List<SearchSuggestion>>()
                    val searchView = mock<FloatingSearchView> {
                        doNothing().whenever(it).swapSuggestions(captor.capture())
                    }

                    searchView.bindSuggestions(state)

                    verify(searchView).swapSuggestions(any())

                    with(captor.firstValue) {
                        assertEquals(state._results.size, size)
                        assertEquals(state._results, this.map { it.body })
                    }
                }
            }
        })

