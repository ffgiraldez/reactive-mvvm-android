package es.ffgiraldez.comicsearch.query.base.ui

import es.ffgiraldez.comicsearch.comics.domain.Volume
import es.ffgiraldez.comicsearch.navigation.Navigator
import es.ffgiraldez.comicsearch.navigation.Screen
import es.ffgiraldez.comicsearch.query.search.presentation.SuspendSearchViewModel
import es.ffgiraldez.comicsearch.query.sugestion.presentation.SuspendSuggestionViewModel

class QueryScreenDelegate(
        val suggestions: SuspendSuggestionViewModel,
        val search: SuspendSearchViewModel,
        val adapter: QueryVolumeAdapter,
        private val navigator: Navigator
) {
    fun onVolumeSelected(volume: Volume): Unit =
            navigator.to(Screen.Detail(volume))

    fun onQueryChange(new: String): Unit =
            with(suggestions) { query.value = new }

    fun onSuggestionSelected(suggestion: String): Unit =
            with(search) { query.value = suggestion }
}