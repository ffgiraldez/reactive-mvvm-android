package es.ffgiraldez.comicsearch.query.search.presentation

import es.ffgiraldez.comicsearch.comics.ComicRepository
import es.ffgiraldez.comicsearch.comics.Volume
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewModel
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import io.reactivex.Flowable
import org.reactivestreams.Publisher

class SearchViewModel private constructor(
        queryToResult: (Flowable<String>) -> Publisher<QueryViewState<Volume>>
) : QueryViewModel<Volume>(queryToResult) {
    companion object {
        operator fun invoke(repo: ComicRepository): SearchViewModel = SearchViewModel {
            it.switchMap {
                repo.findVolume(it)
                        .map { QueryViewState.result(it) }
                        .startWith(QueryViewState.loading())
            }.startWith(QueryViewState.idle())
        }
    }
}