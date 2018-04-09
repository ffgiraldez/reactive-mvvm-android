package es.ffgiraldez.comicsearch.di

import es.ffgiraldez.comicsearch.comics.ComicRepository
import es.ffgiraldez.comicsearch.comics.data.ComicVineApi
import es.ffgiraldez.comicsearch.navigation.Navigator
import es.ffgiraldez.comicsearch.search.presentation.SearchViewModel
import es.ffgiraldez.comicsearch.sugestion.presentation.SuggestionViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val ACTIVITY_PARAM: String = "activity"

val comicContext = applicationContext {
    factory {
        val okHttp = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor()
                        .apply { level = HttpLoggingInterceptor.Level.BASIC }
                )

        Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ComicVineApi.BASE_URL)
                .client(okHttp.build())
                .build()
                .create(ComicVineApi::class.java)
    }
    factory { ComicRepository(get()) }
    factory { SuggestionViewModel(get()) }
    factory { SearchViewModel(get()) }
    factory { params -> Navigator(params[ACTIVITY_PARAM]) }
}