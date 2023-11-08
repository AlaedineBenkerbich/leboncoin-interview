package fr.leboncoin.albumreader.presentation.model

data class ItemUiModel(
    val id: Int,
    val thumbnailUrl: String,
    val onSelection: (Int) -> Unit
)
