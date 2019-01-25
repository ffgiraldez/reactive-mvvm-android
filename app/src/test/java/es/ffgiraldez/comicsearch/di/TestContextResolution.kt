package es.ffgiraldez.comicsearch.di

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import es.ffgiraldez.comicsearch.comics.di.comicModule
import es.ffgiraldez.comicsearch.navigation.di.navigationModule
import es.ffgiraldez.comicsearch.query.search.di.searchModule
import es.ffgiraldez.comicsearch.query.sugestion.di.suggestionModule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.test.KoinTest
import org.koin.test.checkModules

class TestContextResolution : KoinTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `dry run`() {
        // start Koin
        checkModules(listOf(
                navigationModule,
                comicModule,
                searchModule,
                suggestionModule
        ))
    }
}