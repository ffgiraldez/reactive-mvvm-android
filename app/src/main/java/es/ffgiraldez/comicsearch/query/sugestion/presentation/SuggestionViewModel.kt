package es.ffgiraldez.comicsearch.query.sugestion.presentation

import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewModel
import es.ffgiraldez.comicsearch.query.base.presentation.QueryViewState
import es.ffgiraldez.comicsearch.query.sugestion.data.SuggestionRepository
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit

class SuggestionViewModel private constructor(
        transformer: (Flowable<String>) -> Publisher<QueryViewState<String>>
) : QueryViewModel<String>(transformer) {
    companion object {
        operator fun invoke(repo: SuggestionRepository): SuggestionViewModel = SuggestionViewModel {
            it.debounce(400, TimeUnit.MILLISECONDS)
                    .switchMap { query ->
                        repo.findByTerm(query)
                                .map { suggestions -> QueryViewState.result(suggestions) }
                                .startWith(QueryViewState.loading())
                    }.startWith(QueryViewState.idle())
        }
    }
}