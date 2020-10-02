package es.ffgiraldez.comicsearch.query.base.presentation

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow

open class SuspendQueryStateViewModel<T>(
        transformer: (Flow<String>) -> Flow<QueryViewState<T>>
) : ViewModel() {

    val query: MutableLiveData<String> = MutableLiveData()

    private val _state: LiveData<QueryViewState<T>> = transformer(query.asFlow())
            .asLiveData(viewModelScope.coroutineContext)

    val state: LiveData<QueryViewState<T>>
        get() = _state
}