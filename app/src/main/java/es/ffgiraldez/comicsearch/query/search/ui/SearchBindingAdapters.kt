package es.ffgiraldez.comicsearch.query.search.ui

import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import es.ffgiraldez.comicsearch.comics.domain.Volume
import es.ffgiraldez.comicsearch.platform.gone
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.base.ui.OnVolumeSelectedListener
import es.ffgiraldez.comicsearch.query.base.ui.QuerySearchSuggestion.ResultSuggestion
import es.ffgiraldez.comicsearch.query.base.ui.QueryVolumeAdapter
import es.ffgiraldez.comicsearch.query.base.ui.error
import es.ffgiraldez.comicsearch.query.base.ui.hasError
import es.ffgiraldez.comicsearch.query.base.ui.loading
import es.ffgiraldez.comicsearch.query.base.ui.results
import es.ffgiraldez.comicsearch.query.base.ui.toHumanResponse
import io.reactivex.functions.Consumer

interface ClickConsumer : Consumer<SearchSuggestion>
interface SearchConsumer : Consumer<String>

@BindingAdapter("on_suggestion_click", "on_search", requireAll = false)
fun FloatingSearchView.bindSuggestionClick(clickConsumer: ClickConsumer?, searchConsumer: SearchConsumer?) {
    setOnSearchListener(object : FloatingSearchView.OnSearchListener {
        override fun onSearchAction(currentQuery: String) {
            searchConsumer?.apply { searchConsumer.accept(currentQuery) }
        }

        override fun onSuggestionClicked(searchSuggestion: SearchSuggestion) {
            clickConsumer?.apply {
                when (searchSuggestion) {
                    is ResultSuggestion -> {
                        clickConsumer.accept(searchSuggestion)
                        setSearchFocused(false)
                    }
                }
            }
        }
    })
}

/**
 * Limit scope to apply using RecyclerView as BindingAdapter
 */
@BindingAdapter("adapter", "state_change", "on_selected", requireAll = false)
fun RecyclerView.bindStateData(inputAdapter: QueryVolumeAdapter, data: QueryViewState<Volume>?, consumer: OnVolumeSelectedListener) {
    if (adapter == null) {
        inputAdapter.onVolumeSelectedListener = consumer
        adapter = inputAdapter
    }

    data?.let {
        bindError(data.hasError)
        bindResults(data.results)
    }
}


@BindingAdapter("state_change")
fun FrameLayout.bindStateVisibility(data: QueryViewState<Volume>?) = data?.let { state ->
    state.error.fold({ View.GONE }, { View.VISIBLE }).let { visibility = it }
}

@BindingAdapter("state_change")
fun TextView.bindErrorText(data: QueryViewState<Volume>?) = data?.let { state ->
    state.error.fold({ Unit }, { text = it.toHumanResponse() })
}

@BindingAdapter("state_change")
fun ProgressBar.bindProgress(data: QueryViewState<Volume>?) = data?.let { state ->
    gone(!state.loading)
}

private fun RecyclerView.bindError(error: Boolean): Unit =
        gone(error)

private fun RecyclerView.bindResults(error: List<Volume>): Unit = with(adapter as QueryVolumeAdapter) {
    this.submitList(error)
}