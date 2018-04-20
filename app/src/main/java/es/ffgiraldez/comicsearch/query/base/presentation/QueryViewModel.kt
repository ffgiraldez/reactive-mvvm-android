package es.ffgiraldez.comicsearch.query.base.presentation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import es.ffgiraldez.comicsearch.platform.toFlowable
import io.reactivex.Flowable
import org.reactivestreams.Publisher

open class QueryViewModel<T>(
        transformer: (Flowable<String>) -> Publisher<QueryViewState<T>>
) : ViewModel() {

    val query: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val results: MutableLiveData<List<T>> = MutableLiveData()

    init {
        query.toFlowable()
                .compose { transformer(it) }
                .subscribe {
                    when (it) {
                        QueryViewState.Idle -> applyState(false, emptyList())
                        is QueryViewState.Loading -> applyState(true, emptyList())
                        is QueryViewState.Result -> applyState(false, it.results)
                    }
                }


    }

    private fun applyState(isLoading: Boolean, results: List<T>) {
        this.loading.postValue(isLoading)
        this.results.postValue(results)
    }
}