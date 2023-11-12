package fr.leboncoin.albumreader.data.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {
    @Provides
    @Singleton
    fun provideApi(): Api {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://static.leboncoin.fr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(Api::class.java)
    }
}

@Suppress("unused") // Plug the fake API using `.let(::FakeApi)` in the module
private class FakeApi(val realApi: Api) : Api {

    override suspend fun getPictures(): List<RemotePicture> = listOf(
        RemotePicture(
            albumId = 1,
            id = 1,
            title = "accusamus beatae ad facilis cum similique qui sunt",
            url = "https://via.placeholder.com/600/92c952",
            thumbnailUrl = "https://via.placeholder.com/150/92c952"
        ),
        RemotePicture(
            albumId = 1,
            id = 2,
            title = "reprehenderit est deserunt velit ipsam",
            url = "https://via.placeholder.com/600/771796",
            thumbnailUrl = "https://via.placeholder.com/150/771796"
        ),
        RemotePicture(
            albumId = 1,
            id = 3,
            title = "officia porro iure quia iusto qui ipsa ut modi",
            url = "https://via.placeholder.com/600/24f355",
            thumbnailUrl = "https://via.placeholder.com/150/24f355"
        ),
        RemotePicture(
            albumId = 2,
            id = 51,
            title = "non sunt voluptatem placeat consequuntur rem incidunt",
            url = "https://via.placeholder.com/600/8e973b",
            thumbnailUrl = "https://via.placeholder.com/150/8e973b"
        ),
        RemotePicture(
            albumId = 2,
            id = 52,
            title = "eveniet pariatur quia nobis reiciendis laboriosam ea",
            url = "https://via.placeholder.com/600/121fa4",
            thumbnailUrl = "https://via.placeholder.com/150/121fa4"
        ),
        RemotePicture(
            albumId = 3,
            id = 113,
            title = "hic nulla consectetur",
            url = "https://via.placeholder.com/600/1dff02",
            thumbnailUrl = "https://via.placeholder.com/150/1dff02"
        ),
        RemotePicture(
            albumId = 3,
            id = 114,
            title = "consequatur quaerat sunt et",
            url = "https://via.placeholder.com/600/e79b4e",
            thumbnailUrl = "https://via.placeholder.com/150/e79b4e"
        ),
        RemotePicture(
            albumId = 3,
            id = 115,
            title = "unde minus molestias",
            url = "https://via.placeholder.com/600/da7ddf",
            thumbnailUrl = "https://via.placeholder.com/150/da7ddf"
        ),
        RemotePicture(
            albumId = 3,
            id = 116,
            title = "et iure eius enim explicabo",
            url = "https://via.placeholder.com/600/aac33b",
            thumbnailUrl = "https://via.placeholder.com/150/aac33b"
        ),
        RemotePicture(
            albumId = 3,
            id = 117,
            title = "dolore quo nemo omnis odio et iure explicabo",
            url = "https://via.placeholder.com/600/b2fe8",
            thumbnailUrl = "https://via.placeholder.com/150/b2fe8"
        )
    )
}