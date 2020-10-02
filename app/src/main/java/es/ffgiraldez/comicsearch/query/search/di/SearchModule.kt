package es.ffgiraldez.comicsearch.query.search.di

import es.ffgiraldez.comicsearch.query.search.data.SuspendSearchLocalDataSource
import es.ffgiraldez.comicsearch.query.search.data.SuspendSearchRemoteDataSource
import es.ffgiraldez.comicsearch.query.search.data.SuspendSearchRepository
import es.ffgiraldez.comicsearch.query.search.presentation.SuspendSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    factory { SuspendSearchLocalDataSource(get(parameters = { it })) }
    factory { SuspendSearchRemoteDataSource(get()) }
    factory { SuspendSearchRepository(get(parameters = { it }), get()) }
    viewModel { SuspendSearchViewModel(get(parameters = { it })) }
}