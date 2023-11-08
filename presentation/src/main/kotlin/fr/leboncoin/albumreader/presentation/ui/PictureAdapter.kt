package fr.leboncoin.albumreader.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import fr.leboncoin.albumreader.presentation.databinding.ItemPictureBinding
import fr.leboncoin.albumreader.presentation.model.ItemUiModel

internal class PictureAdapter : ListAdapter<ItemUiModel, ItemViewHolder>(DiffCallBack()) {

    companion object {
        private class DiffCallBack : DiffUtil.ItemCallback<ItemUiModel>() {
            override fun areItemsTheSame(
                oldItem: ItemUiModel,
                newItem: ItemUiModel
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ItemUiModel,
                newItem: ItemUiModel
            ): Boolean = oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int =
        currentList.elementAt(position)::class.hashCode()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val attachToParent = false // ViewHolder views should not be attached to parent view group yet
        return ItemViewHolder(ItemPictureBinding.inflate(inflater, parent, attachToParent))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = currentList.elementAt(position)
        holder.onBind(data)
    }
}