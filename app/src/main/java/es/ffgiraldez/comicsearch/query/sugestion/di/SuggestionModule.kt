package es.ffgiraldez.comicsearch.query.sugestion.di

import es.ffgiraldez.comicsearch.query.sugestion.data.*
import es.ffgiraldez.comicsearch.query.sugestion.presentation.SuggestionViewModel
import es.ffgiraldez.comicsearch.query.sugestion.presentation.SuspendSuggestionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val suggestionModule = module {
    factory { SuggestionLocalDataSource(get(parameters = { it })) }
    factory { SuspendSuggestionLocalDataSource(get(parameters = { it })) }
    factory { SuggestionRemoteDataSource(get()) }
    factory { SuspendSuggestionRemoteDataSource(get()) }
    factory { SuggestionRepository(get(parameters = { it }), get()) }
    factory { SuspendSuggestionRepository(get(parameters = { it }), get()) }
    viewModel { SuggestionViewModel(get(parameters = { it })) }
    viewModel { SuspendSuggestionViewModel(get(parameters = { it })) }
}