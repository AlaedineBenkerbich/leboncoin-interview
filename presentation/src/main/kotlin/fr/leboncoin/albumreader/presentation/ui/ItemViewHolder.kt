package fr.leboncoin.albumreader.presentation.ui

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import fr.leboncoin.albumreader.presentation.R
import fr.leboncoin.albumreader.presentation.databinding.ItemPictureBinding
import fr.leboncoin.albumreader.presentation.model.ItemUiModel

internal class ItemViewHolder(private val binding: ItemPictureBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(model: ItemUiModel) {
        binding.thumbnail.load(data = model.thumbnailUrl.toUri()) {
            crossfade(true)
            placeholder(R.drawable.ic_picture)
            fallback(R.drawable.ic_picture)
            error(R.drawable.ic_picture)
        }
        itemView.setOnClickListener { model.onSelection(model.id) }
    }
}