package es.ffgiraldez.comicsearch

import android.app.Application
import com.facebook.stetho.Stetho
import es.ffgiraldez.comicsearch.comics.di.comicModule
import es.ffgiraldez.comicsearch.navigation.di.navigationModule
import es.ffgiraldez.comicsearch.query.search.di.searchModule
import es.ffgiraldez.comicsearch.query.sugestion.di.suggestionModule
import org.koin.android.ext.android.startKoin

class ComicApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(
                navigationModule,
                comicModule,
                searchModule,
                suggestionModule
        ))
        Stetho.initializeWithDefaults(this)
    }
}