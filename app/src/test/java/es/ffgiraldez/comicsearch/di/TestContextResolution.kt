package es.ffgiraldez.comicsearch.di

import android.app.Activity
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.test.KoinTest
import org.koin.test.dryRun

class TestContextResolution : KoinTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    @Test
    fun `dry run`() {
        // start Koin
        startKoin(listOf(comicContext))
        // dry run of given module list
        dryRun(defaultParameters = {
            mapOf(
                    ACTIVITY_PARAM to mock<Activity>(),
                    CONTEXT_PARAM to mock<Context>()
            )
        })
    }
}