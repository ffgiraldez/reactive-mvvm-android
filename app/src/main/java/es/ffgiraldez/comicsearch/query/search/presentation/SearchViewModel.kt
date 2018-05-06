package es.ffgiraldez.comicsearch.query.search.presentation

import es.ffgiraldez.comicsearch.comics.domain.Volume
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewModel
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.search.data.SearchRepository
import io.reactivex.Flowable
import org.reactivestreams.Publisher

class SearchViewModel private constructor(
        queryToResult: (Flowable<String>) -> Publisher<QueryViewState<Volume>>
) : QueryViewModel<Volume>(queryToResult) {
    companion object {
        operator fun invoke(repo: SearchRepository): SearchViewModel = SearchViewModel {
            it.switchMap {
                repo.findByTerm(it)
                        .map { QueryViewState.result(it) }
                        .startWith(QueryViewState.loading())
            }.startWith(QueryViewState.idle())
        }
    }
}