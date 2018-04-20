package es.ffgiraldez.comicsearch.query.base.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import es.ffgiraldez.comicsearch.R
import es.ffgiraldez.comicsearch.databinding.QueryActivityBinding
import es.ffgiraldez.comicsearch.di.ACTIVITY_PARAM
import es.ffgiraldez.comicsearch.di.CONTEXT_PARAM
import es.ffgiraldez.comicsearch.navigation.Navigator
import es.ffgiraldez.comicsearch.query.search.presentation.SearchViewModel
import es.ffgiraldez.comicsearch.query.sugestion.presentation.SuggestionViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

class QueryActivity : AppCompatActivity() {

    private val suggestionViewModel by viewModel<SuggestionViewModel>(parameters = { mapOf(CONTEXT_PARAM to this.applicationContext) })
    private val searchViewModel by viewModel<SearchViewModel>(parameters = { mapOf(CONTEXT_PARAM to this.applicationContext) })
    private val navigator by inject<Navigator>(parameters = { mapOf(ACTIVITY_PARAM to this) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<QueryActivityBinding>(this, R.layout.query_activity).also {
            it.setLifecycleOwner(this@QueryActivity)
            it.delegate = QueryScreenDelegate(suggestionViewModel, searchViewModel, QueryVolumeAdapter(), navigator)
        }
    }
}
