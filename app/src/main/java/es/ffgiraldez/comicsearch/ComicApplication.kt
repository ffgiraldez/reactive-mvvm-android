package es.ffgiraldez.comicsearch

import android.app.Application
import com.facebook.stetho.Stetho
import es.ffgiraldez.comicsearch.di.comicContext
import org.koin.android.ext.android.startKoin

class ComicApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(comicContext))
        Stetho.initializeWithDefaults(this)
    }
}