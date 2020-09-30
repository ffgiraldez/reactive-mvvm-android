package es.ffgiraldez.comicsearch.query.sugestion.ui

import androidx.databinding.BindingAdapter
import com.arlib.floatingsearchview.FloatingSearchView
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState.Error
import es.ffgiraldez.comicsearch.query.base.ui.QuerySearchSuggestion.ResultSuggestion
import es.ffgiraldez.comicsearch.query.base.ui.loading
import es.ffgiraldez.comicsearch.query.base.ui.results
import es.ffgiraldez.comicsearch.query.base.ui.toHumanResponse

@BindingAdapter("on_change")
fun FloatingSearchView.bindQueryChangeListener(
        listener: FloatingSearchView.OnQueryChangeListener
): Unit = setOnQueryChangeListener(listener)

@BindingAdapter("state_change")
fun FloatingSearchView.bindSuggestions(data: QueryViewState<String>?) = data?.run {
    toggleProgress(loading)
    swapSuggestions(suggestions)
}

private val QueryViewState<String>.suggestions: List<ResultSuggestion>
    get() = when (this) {
        is Error -> listOf(ResultSuggestion(_error.toHumanResponse()))
        else -> results.map { ResultSuggestion(it) }
    }

private fun FloatingSearchView.toggleProgress(show: Boolean): Unit = when (show) {
    true -> showProgress()
    false -> hideProgress()
}
