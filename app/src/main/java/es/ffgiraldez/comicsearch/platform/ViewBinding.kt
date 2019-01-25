package es.ffgiraldez.comicsearch.platform

import androidx.databinding.BindingAdapter
import android.view.View

@BindingAdapter("gone")
fun bindImage(view: View, gone: Boolean) = with(view) {
    visibility = when (gone) {
        true -> View.GONE
        false -> View.VISIBLE
    }
}