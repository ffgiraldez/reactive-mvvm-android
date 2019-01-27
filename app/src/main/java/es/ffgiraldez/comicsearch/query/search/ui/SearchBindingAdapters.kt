package es.ffgiraldez.comicsearch.query.search.ui

import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import arrow.core.Option
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.comics.domain.Volume
import es.ffgiraldez.comicsearch.platform.gone
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.base.ui.OnVolumeSelectedListener
import es.ffgiraldez.comicsearch.query.base.ui.QuerySearchSuggestion.ResultSuggestion
import es.ffgiraldez.comicsearch.query.base.ui.QueryVolumeAdapter
import es.ffgiraldez.comicsearch.query.base.ui.toHumanResponse
import io.reactivex.functions.Consumer

interface ClickConsumer : Consumer<SearchSuggestion>
interface SearchConsumer : Consumer<String>

@BindingAdapter("on_suggestion_click", "on_search", requireAll = false)
fun bindSuggestionClick(search: FloatingSearchView, clickConsumer: ClickConsumer?, searchConsumer: SearchConsumer?) {
    search.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
        override fun onSearchAction(currentQuery: String) {
            searchConsumer?.apply { searchConsumer.accept(currentQuery) }
        }

        override fun onSuggestionClicked(searchSuggestion: SearchSuggestion) {
            clickConsumer?.apply {
                when (searchSuggestion) {
                    is ResultSuggestion -> {
                        clickConsumer.accept(searchSuggestion)
                        search.setSearchFocused(false)
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
fun bindStateData(recycler: RecyclerView, inputAdapter: QueryVolumeAdapter, data: QueryViewState<Volume>?, consumer: OnVolumeSelectedListener) =
        with(recycler) {
            if (adapter == null) {
                inputAdapter.onVolumeSelectedListener = consumer
                adapter = inputAdapter
            }

            data?.let {
                bindError(data.error)
                bindResults(data.results)
            }
        }

@BindingAdapter("state_change")
fun bindStateVisibility(errorContainer: FrameLayout, data: QueryViewState<Volume>?) = data?.let { state ->
    state.error.fold({ View.GONE }, { View.VISIBLE }).let { errorContainer.visibility = it }
}

@BindingAdapter("state_change")
fun bindErrorText(errorText: TextView, data: QueryViewState<Volume>?) = data?.let { state ->
    state.error.fold({ Unit }, { errorText.text = it.toHumanResponse() })
}

@BindingAdapter("state_change")
fun bindProgress(progress: ProgressBar, data: QueryViewState<Volume>?) = data?.let { state ->
    progress.gone(state.loading)
}

private fun RecyclerView.bindError(error: Option<ComicError>): Unit =
        error.fold({ View.VISIBLE }, { View.GONE }).let { this.visibility = it }

private fun RecyclerView.bindResults(error: List<Volume>): Unit = with(adapter as QueryVolumeAdapter) {
    this.submitList(error)
}