package es.ffgiraldez.comicsearch.sugestion.ui

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VolumeSearchSuggestion(
        private val volume: String
) : SearchSuggestion {
    override fun getBody(): String = volume
}