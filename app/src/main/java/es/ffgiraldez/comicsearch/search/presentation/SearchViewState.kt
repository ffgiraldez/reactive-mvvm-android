package es.ffgiraldez.comicsearch.search.presentation

import es.ffgiraldez.comicsearch.comics.Volume

sealed class SearchViewState {

    companion object {
        fun result(volumeList: List<Volume>): SearchViewState = Result(volumeList)
        fun idle(): SearchViewState = Idle
        fun loading(): SearchViewState = Loading
    }

    object Idle : SearchViewState()
    object Loading : SearchViewState()
    data class Result(val volumeList: List<Volume>) : SearchViewState()

}