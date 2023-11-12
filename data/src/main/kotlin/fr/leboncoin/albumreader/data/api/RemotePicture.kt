package fr.leboncoin.albumreader.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param albumId The album unique identifier.
 * @param id The picture unique identifier.
 * @param title The picture title.
 * @param url The picture URL.
 * @param thumbnailUrl The picture thumbnail URL.
 */
@Serializable
data class RemotePicture(
    @SerialName("albumId")
    val albumId: Int,

    @SerialName("id")
    val id: Int,

    @SerialName("title")
    val title: String,

    @SerialName("url")
    val url: String,

    @SerialName("thumbnailUrl")
    val thumbnailUrl: String
)