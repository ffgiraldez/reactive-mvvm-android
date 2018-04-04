package es.ffgiraldez.comicsearch.sugestion.presentation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import es.ffgiraldez.comicsearch.comics.ComicRepository
import es.ffgiraldez.comicsearch.platform.toObservable
import java.util.concurrent.TimeUnit

class SuggestionViewModel(
        private val repo: ComicRepository
) : ViewModel() {

    val query: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val results: MutableLiveData<List<String>> = MutableLiveData()

    init {
        query.toObservable()
                .debounce(400, TimeUnit.MILLISECONDS)
                .doOnNext { loading.postValue(true) }
                .doOnNext { results.postValue(emptyList()) }
                .switchMapSingle { repo.searchSuggestion(it) }
                .doOnSubscribe { loading.value = false }
                .doOnSubscribe { results.value = emptyList() }
                .doOnNext { loading.postValue(false) }
                .subscribe {
                    Log.d("cambio", "$it")
                    results.postValue(it)
                }
    }

    fun onQueryChange(new: String) = with(query) { value = new }

}