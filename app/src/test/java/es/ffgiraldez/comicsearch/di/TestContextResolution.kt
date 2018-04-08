package es.ffgiraldez.comicsearch.di

import org.junit.Test
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.test.KoinTest
import org.koin.test.dryRun

class TestContextResolution : KoinTest {
    @Test
    fun `dry run`() {
        // start Koin
        startKoin(listOf(comicContext))
        // dry run of given module list
        dryRun()
    }
}