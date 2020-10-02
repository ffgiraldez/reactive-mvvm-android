package es.ffgiraldez.comicsearch.query.search.ui

import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.FloatingSearchView.OnSearchListener
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import es.ffgiraldez.comicsearch.comics.gen.searchErrorViewState
import es.ffgiraldez.comicsearch.comics.gen.searchViewState
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.base.ui.OnVolumeSelectedListener
import es.ffgiraldez.comicsearch.query.base.ui.QuerySearchSuggestion.ErrorSuggestion
import es.ffgiraldez.comicsearch.query.base.ui.QuerySearchSuggestion.ResultSuggestion
import es.ffgiraldez.comicsearch.query.base.ui.QueryVolumeAdapter
import es.ffgiraldez.comicsearch.query.base.ui.results
import es.ffgiraldez.comicsearch.query.base.ui.toHumanResponse
import io.kotest.core.spec.style.WordSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll


class SearchBindingAdapterSpec : WordSpec({
    "ProgressBar" should {
        "not have interaction on null state" {
            val progressBar = mock<ProgressBar>()

            progressBar.bindProgress(null)

            verifyZeroInteractions(progressBar)
        }

        "be visible on loading state" {
            val progressBar = mock<ProgressBar>()

            progressBar.bindProgress(QueryViewState.loading())

            verify(progressBar).visibility = eq(View.VISIBLE)
        }

        "be gone on non loading state" {
            checkAll(Arb.searchViewState().filterNot { it is QueryViewState.Loading }) { state ->
                val progressBar = mock<ProgressBar>()

                progressBar.bindProgress(state)

                verify(progressBar).visibility = eq(View.GONE)
            }
        }

    }

    "TextView" should {

        "not have interaction on null state" {
            val textView = mock<TextView>()

            textView.bindErrorText(null)

            verifyZeroInteractions(textView)
        }

        "not have interaction on non error state" {
            checkAll(Arb.searchViewState().filterNot { it is QueryViewState.Error }) { state ->
                val textView = mock<TextView>()

                textView.bindErrorText(state)

                verifyZeroInteractions(textView)
            }
        }

        "show error human description on error state" {
            checkAll(Arb.searchErrorViewState()) { state ->
                val textView = mock<TextView>()

                textView.bindErrorText(state)

                verify(textView).text = eq(state._error.toHumanResponse())
            }
        }
    }

    "FrameLayout" should {
        "not have interaction on null state" {
            val frameLayout = mock<FrameLayout>()

            frameLayout.bindStateVisibility(null)

            verifyZeroInteractions(frameLayout)
        }

        "be gone on non error state" {
            checkAll(Arb.searchViewState().filterNot { it is QueryViewState.Error }) { state ->
                val frameLayout = mock<FrameLayout>()

                frameLayout.bindStateVisibility(state)


                verify(frameLayout).visibility = eq(View.GONE)
            }
        }

        "be visible on error state" {
            checkAll(Arb.searchErrorViewState()) { state ->
                val frameLayout = mock<FrameLayout>()

                frameLayout.bindStateVisibility(state)


                verify(frameLayout).visibility = eq(View.VISIBLE)
            }
        }
    }

    "RecyclerView" should {
        "configure is adapter when is not defined" {
            val recyclerView = mock<RecyclerView>()
            val adapter = mock<QueryVolumeAdapter>()
            val onVolumeSelected = mock<OnVolumeSelectedListener>()

            recyclerView.bindStateData(adapter, null, onVolumeSelected)

            verify(recyclerView).adapter = eq(adapter)
            verify(adapter).onVolumeSelectedListener = eq(onVolumeSelected)
        }

        "update result list on state change" {
            checkAll(Arb.searchViewState()) { state ->
                val adapter = mock<QueryVolumeAdapter>()
                val recyclerView = mock<RecyclerView> {
                    on { getAdapter() }.thenReturn(adapter)
                }
                val onVolumeSelected = mock<OnVolumeSelectedListener>()

                recyclerView.bindStateData(adapter, state, onVolumeSelected)

                verify(adapter).submitList(eq(state.results))
            }
        }


        "be visible on non error state" {
            checkAll(Arb.searchViewState().filterNot { it is QueryViewState.Error }) { state ->
                val adapter = mock<QueryVolumeAdapter>()
                val recyclerView = mock<RecyclerView> {
                    on { getAdapter() }.thenReturn(adapter)
                }
                val onVolumeSelected = mock<OnVolumeSelectedListener>()

                recyclerView.bindStateData(adapter, state, onVolumeSelected)

                verify(recyclerView).visibility = eq(View.VISIBLE)
            }
        }

        "be gone on non error state" {
            checkAll(Arb.searchErrorViewState()) { state ->
                val adapter = mock<QueryVolumeAdapter>()
                val recyclerView = mock<RecyclerView> {
                    on { getAdapter() }.thenReturn(adapter)
                }
                val onVolumeSelected = mock<OnVolumeSelectedListener>()

                recyclerView.bindStateData(adapter, state, onVolumeSelected)

                verify(recyclerView).visibility = eq(View.GONE)
            }
        }
    }

    "FloatingView" should {
        "avoid search on error suggestion click" {
            val searchListenerCaptor = argumentCaptor<OnSearchListener>()
            val searchView = mock<FloatingSearchView> {
                doNothing().whenever(it).setOnSearchListener(searchListenerCaptor.capture())
            }
            val click = mock<ClickConsumer>()


            searchView.bindSuggestionClick(click, mock())

            searchListenerCaptor.firstValue.onSuggestionClicked(ErrorSuggestion(Arb.string().next()))

            verifyZeroInteractions(click)
        }

        "search on result suggestion click" {
            val searchListenerCaptor = argumentCaptor<OnSearchListener>()
            val searchView = mock<FloatingSearchView> {
                doNothing().whenever(it).setOnSearchListener(searchListenerCaptor.capture())
            }
            val click = mock<ClickConsumer>()
            val suggestion = ResultSuggestion(Arb.string().next())


            searchView.bindSuggestionClick(click, mock())

            searchListenerCaptor.firstValue.onSuggestionClicked(suggestion)

            verify(click).accept(eq(suggestion))
        }
    }
})
