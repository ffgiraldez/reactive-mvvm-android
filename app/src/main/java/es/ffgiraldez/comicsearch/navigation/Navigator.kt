package es.ffgiraldez.comicsearch.navigation

import android.content.Context
import android.content.Intent
import es.ffgiraldez.comicsearch.detail.DetailActivity

class Navigator(
        private val context: Context
) {
    fun to(screen: Screen) {
        val intent = when (screen) {
            is Screen.Detail -> Intent(context, DetailActivity::class.java)
        }
        context.startActivity(intent)
    }
}

