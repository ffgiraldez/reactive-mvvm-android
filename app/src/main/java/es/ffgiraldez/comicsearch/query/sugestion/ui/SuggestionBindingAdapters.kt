package es.ffgiraldez.comicsearch.query.sugestion.ui

import androidx.databinding.BindingAdapter
import arrow.core.Option
import com.arlib.floatingsearchview.FloatingSearchView
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.platform.safe
import es.ffgiraldez.comicsearch.query.base.ui.QuerySearchSuggestion.ErrorSuggestion
import es.ffgiraldez.comicsearch.query.base.ui.QuerySearchSuggestion.ResultSuggestion
import es.ffgiraldez.comicsearch.query.base.ui.toHumanResponse

@BindingAdapter("on_change")
fun bindQueryChangeListener(search: FloatingSearchView, listener: FloatingSearchView.OnQueryChangeListener) =
        search.setOnQueryChangeListener(listener)

@BindingAdapter("suggestions", "error")
fun bindSuggestions(
        search: FloatingSearchView,
        resultData: List<String>?,
        errorData: Option<ComicError>?
) = safe(errorData, resultData) { error, results ->
    error.fold({
        results.map { ResultSuggestion(it) }
    }, {
        listOf(ErrorSuggestion(it.toHumanResponse()))
    }).let { search.swapSuggestions(it) }
}

@BindingAdapter("show_progress")
fun bindLoading(search: FloatingSearchView, liveData: Boolean?) = liveData?.let {
    when (it) {
        true -> search.showProgress()
        false -> search.hideProgress()
    }
}

