package es.ffgiraldez.comicsearch.query.base.ui

import es.ffgiraldez.comicsearch.comics.Volume
import es.ffgiraldez.comicsearch.navigation.Navigator
import es.ffgiraldez.comicsearch.navigation.Screen
import es.ffgiraldez.comicsearch.query.search.presentation.SearchViewModel
import es.ffgiraldez.comicsearch.query.sugestion.presentation.SuggestionViewModel

class QueryScreenDelegate(
        val suggestions: SuggestionViewModel,
        val search: SearchViewModel,
        val adapter: QueryVolumeAdapter,
        private val navigator: Navigator
) {
    fun onVolumeSelected(volume: Volume) =
            navigator.to(Screen.Detail(volume))

    fun onQueryChange(new: String) =
            with(suggestions) { query.value = new }

    fun onSuggestionSelected(suggestion: String) =
            with(search) { query.value = suggestion }
}