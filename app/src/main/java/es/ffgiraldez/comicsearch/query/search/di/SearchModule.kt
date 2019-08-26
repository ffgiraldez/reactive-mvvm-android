package es.ffgiraldez.comicsearch.query.search.di

import es.ffgiraldez.comicsearch.query.search.data.SearchLocalDataSource
import es.ffgiraldez.comicsearch.query.search.data.SearchRemoteDataSource
import es.ffgiraldez.comicsearch.query.search.data.SearchRepository
import es.ffgiraldez.comicsearch.query.search.presentation.SearchViewModel
import org.koin.dsl.module

val searchModule = module {
    factory { SearchLocalDataSource(get(parameters = { it })) }
    factory { SearchRemoteDataSource(get()) }
    factory { SearchRepository(get(parameters = { it }), get()) }
    factory { SearchViewModel(get(parameters = { it })) }
}