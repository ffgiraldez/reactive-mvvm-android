package es.ffgiraldez.comicsearch.comics.di

import android.arch.persistence.room.Room
import es.ffgiraldez.comicsearch.comics.data.network.ComicVineApi
import es.ffgiraldez.comicsearch.comics.data.storage.ComicDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.context.ParameterProvider
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val ACTIVITY_PARAM: String = "activity"
const val CONTEXT_PARAM: String = "context"

val comicModule = applicationContext {
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
    bean { params: ParameterProvider -> Room.databaseBuilder(params[CONTEXT_PARAM], ComicDatabase::class.java, "comics").build() }
}