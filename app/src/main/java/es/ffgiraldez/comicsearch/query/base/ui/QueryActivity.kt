package es.ffgiraldez.comicsearch.query.base.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import es.ffgiraldez.comicsearch.R
import es.ffgiraldez.comicsearch.databinding.QueryActivityBinding
import es.ffgiraldez.comicsearch.navigation.Navigator
import es.ffgiraldez.comicsearch.query.search.presentation.SearchViewModel
import es.ffgiraldez.comicsearch.query.sugestion.presentation.SuggestionViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class QueryActivity : AppCompatActivity() {

    private val navigator by inject<Navigator> { parametersOf(this) }
    private val suggestionViewModel by viewModel<SuggestionViewModel> { parametersOf(this.applicationContext) }
    private val searchViewModel by viewModel<SearchViewModel> { parametersOf(this.applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<QueryActivityBinding>(this, R.layout.query_activity).also {
            it.setLifecycleOwner(this@QueryActivity)
            it.delegate = QueryScreenDelegate(suggestionViewModel, searchViewModel, QueryVolumeAdapter(), navigator)
        }
    }
}
