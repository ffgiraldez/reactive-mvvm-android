package es.ffgiraldez.comicsearch.sugestion.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.util.Log
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion


@BindingAdapter("on_change")
fun bindQueryChangeListener(search: FloatingSearchView, listener: FloatingSearchView.OnQueryChangeListener) =
        search.setOnQueryChangeListener(listener)

@BindingAdapter("suggestions")
fun <T : SearchSuggestion> bindSuggestions(search: FloatingSearchView, liveData: LiveData<List<T>>) =
        liveData.observeForever({
            Log.d("cambio", "tamaño sugestion ${it?.size}")
            search.swapSuggestions(it)
        })

@BindingAdapter("show_progress")
fun bindLoading(search: FloatingSearchView, liveData: LiveData<Boolean>) =
        liveData.observeForever({
            it?.let { if (it) search.showProgress() else search.hideProgress() }
        })

@BindingConversion
fun convertToVolumeSuggestion(input: LiveData<List<String>>): LiveData<List<VolumeSearchSuggestion>> =
        Transformations.map(input, { value -> value.map { VolumeSearchSuggestion(it) } })