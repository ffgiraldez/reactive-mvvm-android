package es.ffgiraldez.comicsearch.sugestion.ui

import android.databinding.BindingAdapter
import com.arlib.floatingsearchview.FloatingSearchView


@BindingAdapter("on_change")
fun bindQueryChangeListener(search: FloatingSearchView, listener: FloatingSearchView.OnQueryChangeListener) =
        search.setOnQueryChangeListener(listener)

@BindingAdapter("suggestions")
fun bindSuggestions(search: FloatingSearchView, liveData: List<String>?) = liveData?.let {
    it.map { VolumeSearchSuggestion(it) }.let { search.swapSuggestions(it) }
}

@BindingAdapter("show_progress")
fun bindLoading(search: FloatingSearchView, liveData: Boolean?) = liveData?.let {
    when (it) {
        true -> search.showProgress()
        false -> search.hideProgress()
    }
}