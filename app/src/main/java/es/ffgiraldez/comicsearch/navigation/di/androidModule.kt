package es.ffgiraldez.comicsearch.navigation.di

import android.app.Activity
import es.ffgiraldez.comicsearch.navigation.Navigator
import org.koin.dsl.module.module

val navigationModule = module {
    factory { (activity: Activity) -> Navigator(activity) }
}