package es.ffgiraldez.comicsearch.query.sugestion.di

import es.ffgiraldez.comicsearch.query.sugestion.data.SuggestionLocalDataSource
import es.ffgiraldez.comicsearch.query.sugestion.data.SuggestionRemoteDataSource
import es.ffgiraldez.comicsearch.query.sugestion.data.SuggestionRepository
import es.ffgiraldez.comicsearch.query.sugestion.presentation.SuggestionViewModel
import org.koin.dsl.module.applicationContext

val suggestionModule = applicationContext {
    factory { SuggestionLocalDataSource(get({ it.values })) }
    factory { SuggestionRemoteDataSource(get()) }
    factory { SuggestionRepository(get({ it.values }), get()) }
    factory { SuggestionViewModel(get({ it.values })) }
}