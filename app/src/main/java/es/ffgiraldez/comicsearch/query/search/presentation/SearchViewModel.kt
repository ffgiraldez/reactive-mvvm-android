package es.ffgiraldez.comicsearch.query.search.presentation

import es.ffgiraldez.comicsearch.comics.domain.Volume
import es.ffgiraldez.comicsearch.query.base.presentation.QueryStateViewModel
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.base.presentation.toViewState
import es.ffgiraldez.comicsearch.query.search.data.SearchRepository
import io.reactivex.Flowable
import org.reactivestreams.Publisher

class SearchViewModel private constructor(
        queryToResult: (Flowable<String>) -> Publisher<QueryViewState<Volume>>
) : QueryStateViewModel<Volume>(queryToResult) {
    companion object {
        operator fun invoke(repo: SearchRepository): SearchViewModel = SearchViewModel {
            it.switchMap { query -> handleQuery(repo, query) }
                    .startWith(QueryViewState.idle())
        }

        private fun handleQuery(repo: SearchRepository, query: String): Flowable<QueryViewState<Volume>> =
                repo.findByTerm(query)
                        .map { it.toViewState() }
                        .startWith(QueryViewState.loading())
    }
}