package es.ffgiraldez.comicsearch.sugestion.presentation

sealed class SuggestionViewState {

    companion object {
        fun result(suggestions: List<String>): SuggestionViewState = Result(suggestions)
        fun idle(): SuggestionViewState = Idle
        fun loading(): SuggestionViewState = Loading
    }

    object Idle : SuggestionViewState()
    object Loading : SuggestionViewState()
    data class Result(val suggestions: List<String>) : SuggestionViewState()

}