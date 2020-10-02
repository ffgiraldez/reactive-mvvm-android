package es.ffgiraldez.comicsearch.comics.di

import android.content.Context
import androidx.room.Room
import es.ffgiraldez.comicsearch.comics.data.network.SuspendComicVineApi
import es.ffgiraldez.comicsearch.comics.data.storage.ComicDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val comicModule = module {
    factory {
        val okHttp = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor()
                        .apply { level = HttpLoggingInterceptor.Level.BASIC }
                )

        Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SuspendComicVineApi.BASE_URL)
                .client(okHttp.build())
                .build()
                .create(SuspendComicVineApi::class.java)
    }
    single { (context: Context) -> Room.databaseBuilder(context, ComicDatabase::class.java, "comics").build() }
}
