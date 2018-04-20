package es.ffgiraldez.comicsearch.query.base.presentation

sealed class QueryViewState<out T> {

    companion object {
        fun <T> result(volumeList: List<T>): QueryViewState<T> = Result(volumeList)
        fun <T> idle(): QueryViewState<T> = Idle
        fun <T> loading(): QueryViewState<T> = Loading
    }

    object Idle : QueryViewState<Nothing>()
    object Loading : QueryViewState<Nothing>()
    data class Result<out T>(val results: List<T>) : QueryViewState<T>()

}