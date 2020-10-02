package es.ffgiraldez.comicsearch.query.search.presentation

import es.ffgiraldez.comicsearch.comics.domain.Volume
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.base.presentation.SuspendQueryStateViewModel
import es.ffgiraldez.comicsearch.query.base.presentation.toViewState
import es.ffgiraldez.comicsearch.query.search.data.SuspendSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class SuspendSearchViewModel private constructor(
        queryToResult: (Flow<String>) -> Flow<QueryViewState<Volume>>
) : SuspendQueryStateViewModel<Volume>(queryToResult) {
    companion object {
        operator fun invoke(repo: SuspendSearchRepository): SuspendSearchViewModel = SuspendSearchViewModel { query ->
            query.flatMapLatest { term ->
                repo.findByTerm(term)
                        .map { it.toViewState() }
                        .onStart { emit(QueryViewState.loading()) }
            }.onStart { emit(QueryViewState.idle()) }
        }
    }
}