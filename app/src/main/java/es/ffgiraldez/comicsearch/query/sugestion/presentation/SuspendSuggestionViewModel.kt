package es.ffgiraldez.comicsearch.query.sugestion.presentation

import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.base.presentation.SuspendQueryStateViewModel
import es.ffgiraldez.comicsearch.query.base.presentation.toViewState
import es.ffgiraldez.comicsearch.query.sugestion.data.SuspendSuggestionRepository
import kotlinx.coroutines.flow.*

class SuspendSuggestionViewModel private constructor(
        transformer: (Flow<String>) -> Flow<QueryViewState<String>>
) : SuspendQueryStateViewModel<String>(transformer) {
    companion object {
        operator fun invoke(repo: SuspendSuggestionRepository): SuspendSuggestionViewModel = SuspendSuggestionViewModel {
            it.debounce(400)
                    .flatMapLatest { query ->
                        handleQuery(query, repo)
                    }.onStart { emit(QueryViewState.idle()) }
                    .distinctUntilChanged()
        }

        private fun handleQuery(
                query: String,
                repo: SuspendSuggestionRepository
        ): Flow<QueryViewState<String>> =
                if (query.isEmpty()) {
                    flowOf(QueryViewState.idle())
                } else {
                    searchSuggestions(repo, query)
                }

        private fun searchSuggestions(
                repo: SuspendSuggestionRepository,
                query: String
        ): Flow<QueryViewState<String>> =
                repo.findByTerm(query)
                        .map { it.toViewState() }
                        .onStart { emit(QueryViewState.loading()) }
    }
}
