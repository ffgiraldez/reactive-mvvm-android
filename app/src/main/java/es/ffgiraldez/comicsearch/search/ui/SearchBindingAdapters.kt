package es.ffgiraldez.comicsearch.search.ui

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import es.ffgiraldez.comicsearch.comics.Volume
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
fun bindData(recycler: RecyclerView, searchAdapter: SearchVolumeAdapter, data: List<Volume>, consumer: OnVolumeSelectedListener) =
        with(recycler) {
            if (adapter == null) {
                adapter = searchAdapter
                searchAdapter.onVolumeSelectedListener = consumer
            }
            searchAdapter.submitList(data)
        }


interface ClickConsumer : Consumer<SearchSuggestion>
interface SearchConsumer : Consumer<String>