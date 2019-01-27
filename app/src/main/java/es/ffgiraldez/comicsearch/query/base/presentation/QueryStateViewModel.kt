package es.ffgiraldez.comicsearch.query.base.presentation


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.ffgiraldez.comicsearch.platform.plus
import es.ffgiraldez.comicsearch.platform.toFlowable
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import org.reactivestreams.Publisher

open class QueryStateViewModel<T>(
        transformer: (Flowable<String>) -> Publisher<QueryViewState<T>>
) : ViewModel() {

    private val _state: MutableLiveData<QueryViewState<T>> = MutableLiveData()
    val state: LiveData<QueryViewState<T>>
        get() = _state
    val query: MutableLiveData<String> = MutableLiveData()

    private val disposable: CompositeDisposable = CompositeDisposable()

    init {
        disposable + query.toFlowable()
                .compose { transformer(it) }
                .subscribe { _state.postValue(it) }
    }

    override fun onCleared(): Unit = disposable.clear()
}

