package es.ffgiraldez.comicsearch.search.presentation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import es.ffgiraldez.comicsearch.comics.ComicRepository
import es.ffgiraldez.comicsearch.comics.Volume
import es.ffgiraldez.comicsearch.platform.toObservable

class SearchViewModel(
        private val repo: ComicRepository
) : ViewModel() {

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val query: MutableLiveData<String> = MutableLiveData()
    val results: MutableLiveData<List<Volume>> = MutableLiveData()

    init {
        query.toObservable()
                .doOnNext { loading.postValue(true) }
                .doOnNext { results.postValue(emptyList()) }
                .switchMapSingle { repo.searchVolume(it) }
                .doOnSubscribe { loading.value = false }
                .doOnSubscribe { results.value = emptyList() }
                .doOnNext { loading.postValue(false) }
                .subscribe {
                    results.postValue(it)
                    Log.d("cambio", "busqueda completa [${it.size}] volumenes encontrados")
                }
    }
}