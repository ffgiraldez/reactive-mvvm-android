package es.ffgiraldez.comicsearch.search.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import es.ffgiraldez.comicsearch.R
import es.ffgiraldez.comicsearch.databinding.SearchActivityBinding
import es.ffgiraldez.comicsearch.di.comicContext
import es.ffgiraldez.comicsearch.sugestion.presentation.SuggestionViewModel
import org.koin.android.architecture.ext.viewModel

class SearchActivity : AppCompatActivity() {

    private val suggestionViewModel by viewModel<SuggestionViewModel>(parameters = { mapOf("lifecycle" to this) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<SearchActivityBinding>(this, R.layout.search_activity).also {
            it.setLifecycleOwner(this@SearchActivity)
            it.suggestionViewModel = suggestionViewModel
        }
    }
}
