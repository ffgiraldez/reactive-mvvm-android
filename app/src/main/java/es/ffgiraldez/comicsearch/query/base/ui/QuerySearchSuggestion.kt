package es.ffgiraldez.comicsearch.query.base.ui

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import kotlinx.android.parcel.Parcelize

sealed class QuerySearchSuggestion(
        private val suggestion: String
) : SearchSuggestion {
    override fun getBody(): String = suggestion

    @Parcelize
    data class ResultSuggestion(val volume: String) : QuerySearchSuggestion(volume)

    @Parcelize
    data class ErrorSuggestion(val volume: String) : QuerySearchSuggestion(volume)

}