package es.ffgiraldez.comicsearch.search.presentation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import es.ffgiraldez.comicsearch.comics.ComicRepository
import es.ffgiraldez.comicsearch.comics.Volume
import es.ffgiraldez.comicsearch.platform.toFlowable

class SearchViewModel(
        private val repo: ComicRepository
) : ViewModel() {

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val query: MutableLiveData<String> = MutableLiveData()
    val results: MutableLiveData<List<Volume>> = MutableLiveData()

    init {
        loading.value = false
        results.value = emptyList()
        query.toFlowable()
                .doOnNext { loading.postValue(true) }
                .doOnNext { results.postValue(emptyList()) }
                .switchMap { repo.findVolume(it) }
                .doOnNext { loading.postValue(false) }
                .subscribe {
                    results.postValue(it)
                    Log.d("cambio", "busqueda completa [${it.size}] volumenes encontrados")
                }
    }
}