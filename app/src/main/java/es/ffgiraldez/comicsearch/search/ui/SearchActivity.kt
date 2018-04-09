package es.ffgiraldez.comicsearch.search.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import es.ffgiraldez.comicsearch.R
import es.ffgiraldez.comicsearch.databinding.SearchActivityBinding
import es.ffgiraldez.comicsearch.di.ACTIVITY_PARAM
import es.ffgiraldez.comicsearch.navigation.Navigator
import es.ffgiraldez.comicsearch.search.presentation.SearchViewModel
import es.ffgiraldez.comicsearch.sugestion.presentation.SuggestionViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

class SearchActivity : AppCompatActivity() {

    private val suggestionViewModel by viewModel<SuggestionViewModel>()
    private val searchViewModel by viewModel<SearchViewModel>()
    private val navigator by inject<Navigator>(parameters = { mapOf(ACTIVITY_PARAM to this) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<SearchActivityBinding>(this, R.layout.search_activity).also {
            it.setLifecycleOwner(this@SearchActivity)
            it.delegate = SearchScreenDelegate(suggestionViewModel, searchViewModel, SearchVolumeAdapter(), navigator)
        }
    }
}
