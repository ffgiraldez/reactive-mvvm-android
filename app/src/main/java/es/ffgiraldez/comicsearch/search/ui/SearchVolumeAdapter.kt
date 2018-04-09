package es.ffgiraldez.comicsearch.search.ui

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import es.ffgiraldez.comicsearch.comics.Volume
import es.ffgiraldez.comicsearch.databinding.SearchItemBinding

class SearchVolumeAdapter : ListAdapter<Volume, SearchVolumeViewHolder>(asyncDiff) {

    var onVolumeSelectedListener: OnVolumeSelectedListener? = null

    companion object {
        val asyncDiff: DiffUtil.ItemCallback<Volume> = object : DiffUtil.ItemCallback<Volume>() {
            override fun areItemsTheSame(oldItem: Volume, newItem: Volume): Boolean = oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: Volume, newItem: Volume): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVolumeViewHolder {
        return LayoutInflater.from(parent.context).run {
            SearchItemBinding.inflate(this, parent, false)
        }.run { SearchVolumeViewHolder(this) }
    }

    override fun onBindViewHolder(holder: SearchVolumeViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { onVolumeSelectedListener?.onVolumeSelected(getItem(position)) }
    }
}

interface OnVolumeSelectedListener {
    fun onVolumeSelected(volume: Volume)
}


class SearchVolumeViewHolder(
        private val binding: SearchItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(volume: Volume) = with(binding) {
        this.volume = volume
        executePendingBindings()
    }
}