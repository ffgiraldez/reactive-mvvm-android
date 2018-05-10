package es.ffgiraldez.comicsearch.query.search.ui

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import arrow.core.Option
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.comics.domain.Volume
import es.ffgiraldez.comicsearch.query.base.ui.OnVolumeSelectedListener
import es.ffgiraldez.comicsearch.query.base.ui.QueryVolumeAdapter
import es.ffgiraldez.comicsearch.query.base.ui.toHumanResponse
import io.reactivex.functions.Consumer

@BindingAdapter("on_suggestion_click", "on_search", requireAll = false)
fun bindSuggestionClick(search: FloatingSearchView, clickConsumer: ClickConsumer?, searchConsumer: SearchConsumer?) {
    search.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
        override fun onSearchAction(currentQuery: String) {
            searchConsumer?.apply { searchConsumer.accept(currentQuery) }
        }

        override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
            clickConsumer?.apply {
                clickConsumer.accept(searchSuggestion)
                search.setSearchFocused(false)
            }
        }
    })
}

/**
 * Limit scope to apply using RecyclerView as BindingAdapter
 */
@BindingAdapter("adapter", "on_data_change", "on_selected", requireAll = false)
fun bindData(recycler: RecyclerView, queryAdapter: QueryVolumeAdapter, data: List<Volume>?, consumer: OnVolumeSelectedListener) =
        with(recycler) {
            if (adapter == null) {
                adapter = queryAdapter
                queryAdapter.onVolumeSelectedListener = consumer
            }
            data?.let { queryAdapter.submitList(data) }
        }

@BindingAdapter("error")
fun bindDataError(recycler: RecyclerView, errorData: Option<ComicError>?) = errorData?.let { error ->
    error.fold({ View.VISIBLE }, { View.GONE }).let { recycler.visibility = it }
}

@BindingAdapter("error")
fun bindErrorVisibility(errorContainer: FrameLayout, errorData: Option<ComicError>?) = errorData?.let { error ->
    error.fold({ View.GONE }, { View.VISIBLE }).let { errorContainer.visibility = it }
}

@BindingAdapter("error")
fun bindErrorText(errorText: TextView, errorData: Option<ComicError>?) = errorData?.let { error ->
    error.fold({ Unit }, { errorText.text = it.toHumanResponse() })
}

interface ClickConsumer : Consumer<SearchSuggestion>
interface SearchConsumer : Consumer<String>