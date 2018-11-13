package es.ffgiraldez.comicsearch.query.sugestion.di

import es.ffgiraldez.comicsearch.query.sugestion.data.SuggestionLocalDataSource
import es.ffgiraldez.comicsearch.query.sugestion.data.SuggestionRemoteDataSource
import es.ffgiraldez.comicsearch.query.sugestion.data.SuggestionRepository
import es.ffgiraldez.comicsearch.query.sugestion.presentation.SuggestionViewModel
import org.koin.dsl.module.module

val suggestionModule = module {
    factory { SuggestionLocalDataSource(get(parameters = { it })) }
    factory { SuggestionRemoteDataSource(get()) }
    factory { SuggestionRepository(get(parameters = { it }), get()) }
    factory { SuggestionViewModel(get(parameters = { it })) }
}