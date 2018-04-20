package es.ffgiraldez.comicsearch.search.presentation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import es.ffgiraldez.comicsearch.comics.ComicRepository
import es.ffgiraldez.comicsearch.comics.Volume
import es.ffgiraldez.comicsearch.platform.toFlowable

class SearchViewModel(
        private val repo: ComicRepository
) : ViewModel() {

    val query: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val results: MutableLiveData<List<Volume>> = MutableLiveData()

    init {
        query.toFlowable()
                .switchMap {
                    repo.findVolume(it)
                            .map { SearchViewState.result(it) }
                            .startWith(SearchViewState.loading())
                }.startWith(SearchViewState.idle())
                .subscribe {
                    when (it) {
                        SearchViewState.Idle -> applyState(false, emptyList())
                        is SearchViewState.Loading -> applyState(true, emptyList())
                        is SearchViewState.Result -> applyState(false, it.volumeList)
                    }
                }

    }

    private fun applyState(isLoading: Boolean, volumeList: List<Volume>) {
        loading.postValue(isLoading)
        results.postValue(volumeList)
    }
}