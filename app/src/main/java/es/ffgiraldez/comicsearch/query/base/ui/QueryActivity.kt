package es.ffgiraldez.comicsearch.query.base.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import es.ffgiraldez.comicsearch.R
import es.ffgiraldez.comicsearch.databinding.QueryActivityBinding
import es.ffgiraldez.comicsearch.navigation.Navigator
import es.ffgiraldez.comicsearch.query.search.presentation.SearchViewModel
import es.ffgiraldez.comicsearch.query.search.presentation.SuspendSearchViewModel
import es.ffgiraldez.comicsearch.query.sugestion.presentation.SuggestionViewModel
import es.ffgiraldez.comicsearch.query.sugestion.presentation.SuspendSuggestionViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class QueryActivity : AppCompatActivity() {

    private val navigator by inject<Navigator> { parametersOf(this) }
    private val suggestionViewModel by viewModel<SuspendSuggestionViewModel> { parametersOf(this.applicationContext) }
    private val searchViewModel by viewModel<SuspendSearchViewModel> { parametersOf(this.applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<QueryActivityBinding>(this, R.layout.query_activity).also {
            it.lifecycleOwner = this
            it.delegate = QueryScreenDelegate(suggestionViewModel, searchViewModel, QueryVolumeAdapter(), navigator)
        }
    }
}
