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
            it.switchMap { handleQuery(repo, it) }
                    .startWith(QueryViewState.idle())
        }

        private fun handleQuery(repo: SearchRepository, it: String): Flowable<QueryViewState<Volume>> =
                repo.findByTerm(it)
                        .map {
                            it.fold({
                                QueryViewState.error<Volume>(it)
                            }, {
                                QueryViewState.result(it)
                            })
                        }
                        .startWith(QueryViewState.loading())
    }
}