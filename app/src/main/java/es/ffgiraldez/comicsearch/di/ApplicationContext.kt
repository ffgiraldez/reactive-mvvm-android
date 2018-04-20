package es.ffgiraldez.comicsearch.di

import android.arch.persistence.room.Room
import es.ffgiraldez.comicsearch.comics.ComicRepository
import es.ffgiraldez.comicsearch.comics.data.ComicVineApi
import es.ffgiraldez.comicsearch.comics.store.ComicDatabase
import es.ffgiraldez.comicsearch.navigation.Navigator
import es.ffgiraldez.comicsearch.query.search.presentation.SearchViewModel
import es.ffgiraldez.comicsearch.query.sugestion.presentation.SuggestionViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.context.ParameterProvider
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val ACTIVITY_PARAM: String = "activity"
const val CONTEXT_PARAM: String = "context"

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
    factory { ComicRepository(get(), get({ it.values })) }
    factory { SuggestionViewModel(get({ it.values })) }
    factory { SearchViewModel(get()) }
    factory { params -> Navigator(params[ACTIVITY_PARAM]) }
    bean { params: ParameterProvider -> Room.databaseBuilder(params[CONTEXT_PARAM], ComicDatabase::class.java, "comics").build() }
}