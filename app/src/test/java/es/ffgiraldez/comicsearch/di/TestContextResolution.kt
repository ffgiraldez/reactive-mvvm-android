package es.ffgiraldez.comicsearch.di

import es.ffgiraldez.comicsearch.comics.di.comicModule
import es.ffgiraldez.comicsearch.navigation.di.navigationModule
import es.ffgiraldez.comicsearch.query.search.di.searchModule
import es.ffgiraldez.comicsearch.query.sugestion.di.suggestionModule
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.context.startKoin
import org.koin.test.KoinTest

@ExtendWith(InstantTaskExtension::class)
class TestContextResolution : KoinTest {

    @Test
    fun `dry run`() {
        // start Koin
        startKoin {
            modules(listOf(
                    navigationModule,
                    comicModule,
                    searchModule,
                    suggestionModule
            ))
        }
    }
}
