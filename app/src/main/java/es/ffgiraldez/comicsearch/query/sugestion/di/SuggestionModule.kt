package es.ffgiraldez.comicsearch.query.sugestion.di

import es.ffgiraldez.comicsearch.query.sugestion.data.SuspendSuggestionLocalDataSource
import es.ffgiraldez.comicsearch.query.sugestion.data.SuspendSuggestionRemoteDataSource
import es.ffgiraldez.comicsearch.query.sugestion.data.SuspendSuggestionRepository
import es.ffgiraldez.comicsearch.query.sugestion.presentation.SuspendSuggestionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val suggestionModule = module {
    factory { SuspendSuggestionLocalDataSource(get(parameters = { it })) }
    factory { SuspendSuggestionRemoteDataSource(get()) }
    factory { SuspendSuggestionRepository(get(parameters = { it }), get()) }
    viewModel { SuspendSuggestionViewModel(get(parameters = { it })) }
}