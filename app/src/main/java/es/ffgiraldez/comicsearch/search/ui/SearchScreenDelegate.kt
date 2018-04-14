package es.ffgiraldez.comicsearch.search.ui

import es.ffgiraldez.comicsearch.comics.Volume
import es.ffgiraldez.comicsearch.navigation.Navigator
import es.ffgiraldez.comicsearch.navigation.Screen
import es.ffgiraldez.comicsearch.search.presentation.SearchViewModel
import es.ffgiraldez.comicsearch.sugestion.presentation.SuggestionViewModel

class SearchScreenDelegate(
        val suggestions: SuggestionViewModel,
        val search: SearchViewModel,
        val adapter: SearchVolumeAdapter,
        private val navigator: Navigator
) {
    fun onVolumeSelected(volume: Volume) =
            navigator.to(Screen.Detail(volume))

    fun onQueryChange(new: String) =
            with(suggestions) { query.value = new }

    fun onSuggestionSelected(suggestion: String) =
            with(search) { query.value = suggestion }
}