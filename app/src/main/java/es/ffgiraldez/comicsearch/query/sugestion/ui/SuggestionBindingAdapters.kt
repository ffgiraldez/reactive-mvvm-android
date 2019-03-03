package es.ffgiraldez.comicsearch.query.sugestion.ui

import androidx.databinding.BindingAdapter
import com.arlib.floatingsearchview.FloatingSearchView
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.base.ui.QuerySearchSuggestion.ErrorSuggestion
import es.ffgiraldez.comicsearch.query.base.ui.QuerySearchSuggestion.ResultSuggestion
import es.ffgiraldez.comicsearch.query.base.ui.error
import es.ffgiraldez.comicsearch.query.base.ui.loading
import es.ffgiraldez.comicsearch.query.base.ui.results
import es.ffgiraldez.comicsearch.query.base.ui.toHumanResponse

@BindingAdapter("on_change")
fun FloatingSearchView.bindQueryChangeListener(
        listener: FloatingSearchView.OnQueryChangeListener
): Unit = setOnQueryChangeListener(listener)

@BindingAdapter("state_change")
fun FloatingSearchView.bindSuggestions(data: QueryViewState<String>?): Unit? = data?.let { state ->
    toggleProgress(state.loading)
    state.error.fold({
        state.results.map { ResultSuggestion(it) }
    }, {
        listOf(ErrorSuggestion(it.toHumanResponse()))
    }).let(::swapSuggestions)
}

private fun FloatingSearchView.toggleProgress(show: Boolean): Unit = when (show) {
    true -> showProgress()
    false -> hideProgress()
}

