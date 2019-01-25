package es.ffgiraldez.comicsearch.query.base.presentation


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.Option
import arrow.core.none
import arrow.core.some
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.platform.toFlowable
import io.reactivex.Flowable
import org.reactivestreams.Publisher

open class QueryViewModel<T>(
        transformer: (Flowable<String>) -> Publisher<QueryViewState<T>>
) : ViewModel() {

    val query: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<Option<ComicError>> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val results: MutableLiveData<List<T>> = MutableLiveData()

    init {
        query.toFlowable()
                .compose { transformer(it) }
                .subscribe {
                    when (it) {
                        is QueryViewState.Loading -> applyState(isLoading = true)
                        is QueryViewState.Idle -> applyState(isLoading = false)
                        is QueryViewState.Error -> applyState(isLoading = false, error = it.error.some())
                        is QueryViewState.Result -> applyState(isLoading = false, results = it.results)
                    }
                }
    }

    private fun applyState(isLoading: Boolean, results: List<T> = emptyList(), error: Option<ComicError> = none()) {
        this.loading.postValue(isLoading)
        this.results.postValue(results)
        this.error.postValue(error)
    }
}