package es.ffgiraldez.comicsearch.platform

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("image")
fun bindImage(image: ImageView, url: String) = Picasso.get().load(url).into(image)