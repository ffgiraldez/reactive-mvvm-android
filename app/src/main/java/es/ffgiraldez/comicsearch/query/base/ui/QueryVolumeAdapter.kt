package es.ffgiraldez.comicsearch.query.base.ui

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import es.ffgiraldez.comicsearch.comics.Volume
import es.ffgiraldez.comicsearch.databinding.QueryItemBinding

class QueryVolumeAdapter : ListAdapter<Volume, QueryVolumeViewHolder>(asyncDiff) {

    var onVolumeSelectedListener: OnVolumeSelectedListener? = null

    companion object {
        val asyncDiff: DiffUtil.ItemCallback<Volume> = object : DiffUtil.ItemCallback<Volume>() {
            override fun areItemsTheSame(oldItem: Volume, newItem: Volume): Boolean = oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: Volume, newItem: Volume): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryVolumeViewHolder {
        return LayoutInflater.from(parent.context).run {
            QueryItemBinding.inflate(this, parent, false)
        }.run { QueryVolumeViewHolder(this) }
    }

    override fun onBindViewHolder(holder: QueryVolumeViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { onVolumeSelectedListener?.onVolumeSelected(getItem(position)) }
    }
}

interface OnVolumeSelectedListener {
    fun onVolumeSelected(volume: Volume)
}


class QueryVolumeViewHolder(
        private val binding: QueryItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(volume: Volume) = with(binding) {
        this.volume = volume
        executePendingBindings()
    }
}