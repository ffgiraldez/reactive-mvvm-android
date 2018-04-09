package es.ffgiraldez.comicsearch.di

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
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
        dryRun(defaultParameters = { mapOf(ACTIVITY_PARAM to mock<Context> {}) })
    }
}