package es.ffgiraldez.comicsearch.sugestion.domain

import es.ffgiraldez.comicsearch.sugestion.data.ComicVineApi
import es.ffgiraldez.comicsearch.sugestion.data.SuggestionResponse
import io.reactivex.Single

class ComicRepository(
        private val api: ComicVineApi
) {
    fun searchSuggestion(query: String): Single<SuggestionResponse> = api.fetchSuggestedVolumes(query)
}

