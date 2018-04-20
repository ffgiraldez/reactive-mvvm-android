package es.ffgiraldez.comicsearch.query.base.ui

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuerySearchSuggestion(
        private val volume: String
) : SearchSuggestion {
    override fun getBody(): String = volume
}