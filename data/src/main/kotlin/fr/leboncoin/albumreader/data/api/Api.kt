package fr.leboncoin.albumreader.data.api

import retrofit2.http.GET

/**
 * API routes
 */
interface Api {

    /**
     * Get the list of pictures sorted by album.
     *
     * @return The list of pictures.
     *
     * @throws HttpException When API responds with HTTP error response.
     * @throws IOException If API call fails (e.g. network interruption).
     * @throws SerializationException On any request serialization or response deserialization error.
     */
    @GET("img/shared/technical-test.json")
    suspend fun getPictures(): List<RemotePicture>
}