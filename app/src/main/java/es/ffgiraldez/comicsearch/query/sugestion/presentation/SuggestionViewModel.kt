package es.ffgiraldez.comicsearch.query.sugestion.presentation

import es.ffgiraldez.comicsearch.query.base.presentation.QueryStateViewModel
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.base.presentation.toViewState
import es.ffgiraldez.comicsearch.query.sugestion.data.SuggestionRepository
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit

class SuggestionViewModel private constructor(
        transformer: (Flowable<String>) -> Publisher<QueryViewState<String>>
) : QueryStateViewModel<String>(transformer) {
    companion object {
        operator fun invoke(repo: SuggestionRepository): SuggestionViewModel = SuggestionViewModel {
            it.debounce(400, TimeUnit.MILLISECONDS)
                    .switchMap { query -> handleQuery(query, repo) }
                    .startWith(QueryViewState.idle())
                    .distinctUntilChanged()
        }

        private fun handleQuery(
                query: String,
                repo: SuggestionRepository
        ): Flowable<QueryViewState<String>> =
                if (query.isEmpty()) {
                    Flowable.just(QueryViewState.idle())
                } else {
                    searchSuggestions(repo, query)
                }

        private fun searchSuggestions(
                repo: SuggestionRepository,
                query: String
        ): Flowable<QueryViewState<String>> =
                repo.findByTerm(query)
                        .map { it.toViewState() }
                        .startWith(QueryViewState.loading())
    }
}
