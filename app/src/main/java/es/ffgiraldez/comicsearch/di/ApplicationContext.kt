package es.ffgiraldez.comicsearch.di

import es.ffgiraldez.comicsearch.comics.ComicRepository
import es.ffgiraldez.comicsearch.comics.data.ComicVineApi
import es.ffgiraldez.comicsearch.search.presentation.SearchViewModel
import es.ffgiraldez.comicsearch.sugestion.presentation.SuggestionViewModel
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


val comicContext = applicationContext {
    bean {
        Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ComicVineApi.BASE_URL)
                .build()
                .create(ComicVineApi::class.java)
    }
    bean { ComicRepository(get()) }
    bean { params -> SuggestionViewModel(params["lifecycle"], get()) }
    bean { params -> SearchViewModel(params["lifecycle"], get()) }
}