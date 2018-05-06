package es.ffgiraldez.comicsearch.query.search.di

import es.ffgiraldez.comicsearch.query.search.data.SearchLocalDataSource
import es.ffgiraldez.comicsearch.query.search.data.SearchRemoteDataSource
import es.ffgiraldez.comicsearch.query.search.data.SearchRepository
import es.ffgiraldez.comicsearch.query.search.presentation.SearchViewModel
import org.koin.dsl.module.applicationContext

val searchModule = applicationContext {
    factory { SearchLocalDataSource(get({ it.values })) }
    factory { SearchRemoteDataSource(get()) }
    factory { SearchRepository(get({ it.values }), get()) }
    factory { SearchViewModel(get()) }
}