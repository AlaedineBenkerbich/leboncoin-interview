package fr.leboncoin.albumreader.domain.model

data class PictureData(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)
