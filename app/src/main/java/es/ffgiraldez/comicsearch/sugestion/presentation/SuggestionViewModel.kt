package es.ffgiraldez.comicsearch.sugestion.presentation

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import es.ffgiraldez.comicsearch.platform.toObservable
import es.ffgiraldez.comicsearch.sugestion.domain.ComicRepository
import java.util.concurrent.TimeUnit

class SuggestionViewModel(
        private val lifecycle: LifecycleOwner,
        private val repo: ComicRepository
) : ViewModel() {

    val query: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val results: MutableLiveData<List<String>> = MutableLiveData()

    init {
        query.toObservable(lifecycle)
                .debounce(400, TimeUnit.MILLISECONDS)
                .doOnNext { loading.postValue(true) }
                .doOnNext { results.postValue(emptyList()) }
                .flatMapSingle { repo.searchSuggestion(it) }
                .map { response ->
                    response.results
                            .distinctBy { it.name }
                            .map {
                                it.name
                            }
                }
                .doOnSubscribe { loading.postValue(false) }
                .doOnNext { loading.postValue(false) }
                .subscribe {
                    Log.d("cambio", "$it")
                    results.postValue(it)
                }
    }

    fun onQueryChange(new: String) = with(query) { value = new }

}