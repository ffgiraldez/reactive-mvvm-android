package es.ffgiraldez.comicsearch.query.search.di

import es.ffgiraldez.comicsearch.query.search.data.*
import es.ffgiraldez.comicsearch.query.search.presentation.SearchViewModel
import es.ffgiraldez.comicsearch.query.search.presentation.SuspendSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    factory { SearchLocalDataSource(get(parameters = { it })) }
    factory { SuspendSearchLocalDataSource(get(parameters = { it })) }
    factory { SearchRemoteDataSource(get()) }
    factory { SuspendSearchRemoteDataSource(get()) }
    factory { SearchRepository(get(parameters = { it }), get()) }
    factory { SuspendSearchRepository(get(parameters = { it }), get()) }
    viewModel { SearchViewModel(get(parameters = { it })) }
    viewModel { SuspendSearchViewModel(get(parameters = { it })) }
}