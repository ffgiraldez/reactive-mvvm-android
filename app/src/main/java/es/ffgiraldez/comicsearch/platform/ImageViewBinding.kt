package es.ffgiraldez.comicsearch.platform

import androidx.databinding.BindingAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

@BindingAdapter("image")
fun bindImage(image: ImageView, url: String) = Picasso.with(image.context).load(url).into(image)