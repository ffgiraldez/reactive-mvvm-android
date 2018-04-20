package es.ffgiraldez.comicsearch.sugestion.presentation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import es.ffgiraldez.comicsearch.comics.ComicRepository
import es.ffgiraldez.comicsearch.platform.toFlowable
import java.util.concurrent.TimeUnit

class SuggestionViewModel(
        private val repo: ComicRepository
) : ViewModel() {

    val query: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val results: MutableLiveData<List<String>> = MutableLiveData()

    init {
        query.toFlowable()
                .debounce(400, TimeUnit.MILLISECONDS)
                .switchMap { query ->
                    repo.findSuggestion(query)
                            .map { suggestions -> SuggestionViewState.result(suggestions) }
                            .startWith(SuggestionViewState.loading())
                }.startWith(SuggestionViewState.idle())
                .subscribe {
                    when (it) {
                        SuggestionViewState.Idle -> applyState(false, emptyList())
                        is SuggestionViewState.Loading -> applyState(true, emptyList())
                        is SuggestionViewState.Result -> applyState(false, it.suggestions)
                    }
                }

    }

    private fun applyState(isLoading: Boolean, suggestions: List<String>) {
        loading.postValue(isLoading)
        results.postValue(suggestions)
    }
}