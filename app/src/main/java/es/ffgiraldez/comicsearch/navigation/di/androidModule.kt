package es.ffgiraldez.comicsearch.navigation.di

import es.ffgiraldez.comicsearch.navigation.Navigator
import org.koin.dsl.module.applicationContext

const val ACTIVITY_PARAM: String = "activity"
const val CONTEXT_PARAM: String = "context"

val navigationModule = applicationContext {
    factory { params -> Navigator(params[ACTIVITY_PARAM]) }
}