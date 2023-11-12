package fr.leboncoin.albumreader.data.repository

import fr.leboncoin.albumreader.data.api.Api
import fr.leboncoin.albumreader.data.api.RemotePicture
import fr.leboncoin.albumreader.data.database.Dao
import fr.leboncoin.albumreader.data.database.PictureEntity
import fr.leboncoin.albumreader.domain.model.PictureData
import fr.leboncoin.albumreader.domain.repository.PicturesRepository.FetchResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException
import kotlin.test.assertEquals

/**
 * Unit tests of [PicturesRepositoryImpl].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PicturesRepositoryImplTest {

    private val mockApi: Api = mockk()
    private val mockDao: Dao = mockk()

    private val repository: PicturesRepositoryImpl by lazy {
        PicturesRepositoryImpl(
            mockApi,
            mockDao,
            UnconfinedTestDispatcher()
        )
    }

    private val localPictures = listOf(
        PictureEntity(
            albumId = 1,
            id = 1,
            title = "accusamus beatae ad facilis cum similique qui sunt",
            url = "https://via.placeholder.com/600/92c952",
            thumbnailUrl = "https://via.placeholder.com/150/92c952"
        ),
        PictureEntity(
            albumId = 1,
            id = 2,
            title = "reprehenderit est deserunt velit ipsam",
            url = "https://via.placeholder.com/600/771796",
            thumbnailUrl = "https://via.placeholder.com/150/771796"
        ),
        PictureEntity(
            albumId = 1,
            id = 3,
            title = "officia porro iure quia iusto qui ipsa ut modi",
            url = "https://via.placeholder.com/600/24f355",
            thumbnailUrl = "https://via.placeholder.com/150/24f355"
        ),
        PictureEntity(
            albumId = 2,
            id = 51,
            title = "non sunt voluptatem placeat consequuntur rem incidunt",
            url = "https://via.placeholder.com/600/8e973b",
            thumbnailUrl = "https://via.placeholder.com/150/8e973b"
        ),
        PictureEntity(
            albumId = 2,
            id = 52,
            title = "eveniet pariatur quia nobis reiciendis laboriosam ea",
            url = "https://via.placeholder.com/600/121fa4",
            thumbnailUrl = "https://via.placeholder.com/150/121fa4"
        ),
        PictureEntity(
            albumId = 3,
            id = 113,
            title = "hic nulla consectetur",
            url = "https://via.placeholder.com/600/1dff02",
            thumbnailUrl = "https://via.placeholder.com/150/1dff02"
        ),
        PictureEntity(
            albumId = 3,
            id = 114,
            title = "consequatur quaerat sunt et",
            url = "https://via.placeholder.com/600/e79b4e",
            thumbnailUrl = "https://via.placeholder.com/150/e79b4e"
        ),
        PictureEntity(
            albumId = 3,
            id = 115,
            title = "unde minus molestias",
            url = "https://via.placeholder.com/600/da7ddf",
            thumbnailUrl = "https://via.placeholder.com/150/da7ddf"
        ),
        PictureEntity(
            albumId = 3,
            id = 116,
            title = "et iure eius enim explicabo",
            url = "https://via.placeholder.com/600/aac33b",
            thumbnailUrl = "https://via.placeholder.com/150/aac33b"
        ),
        PictureEntity(
            albumId = 3,
            id = 117,
            title = "dolore quo nemo omnis odio et iure explicabo",
            url = "https://via.placeholder.com/600/b2fe8",
            thumbnailUrl = "https://via.placeholder.com/150/b2fe8"
        )
    )

    @Test
    fun `should invoke local database when fetching pictures`() = runTest {
        // Given
        coEvery { mockDao.getPictures() } returns localPictures

        // When
        repository.getPictures().collect()

        // Then
        coVerify(exactly = 1) { mockDao.getPictures() }
        confirmVerified(mockDao)
    }

    @Test
    fun `should emit Progress + Error results when fetching pictures and call to local database fails`() = runTest {
        // Given
        val databaseError = Exception()
        coEvery { mockDao.getPictures() } coAnswers { throw databaseError }

        // When
        val collectedGetResults = repository.getPictures().toList()

        // Then
        assertEquals(
            listOf(
                FetchResult.Progress,
                FetchResult.Error
            ),
            collectedGetResults
        )
    }

    @Test
    fun `should emit Progress + Success results with picture list when fetching pictures and local database contains pictures`() =
        runTest {
            // Given
            coEvery { mockDao.getPictures() } returns localPictures

            // When
            val collectedGetResults = repository.getPictures().toList()

            // Then
            assertEquals(
                listOf(
                    FetchResult.Progress,
                    FetchResult.Success(
                        listOf(
                            PictureData(
                                albumId = 1,
                                id = 1,
                                title = "accusamus beatae ad facilis cum similique qui sunt",
                                url = "https://via.placeholder.com/600/92c952",
                                thumbnailUrl = "https://via.placeholder.com/150/92c952"
                            ),
                            PictureData(
                                albumId = 1,
                                id = 2,
                                title = "reprehenderit est deserunt velit ipsam",
                                url = "https://via.placeholder.com/600/771796",
                                thumbnailUrl = "https://via.placeholder.com/150/771796"
                            ),
                            PictureData(
                                albumId = 1,
                                id = 3,
                                title = "officia porro iure quia iusto qui ipsa ut modi",
                                url = "https://via.placeholder.com/600/24f355",
                                thumbnailUrl = "https://via.placeholder.com/150/24f355"
                            ),
                            PictureData(
                                albumId = 2,
                                id = 51,
                                title = "non sunt voluptatem placeat consequuntur rem incidunt",
                                url = "https://via.placeholder.com/600/8e973b",
                                thumbnailUrl = "https://via.placeholder.com/150/8e973b"
                            ),
                            PictureData(
                                albumId = 2,
                                id = 52,
                                title = "eveniet pariatur quia nobis reiciendis laboriosam ea",
                                url = "https://via.placeholder.com/600/121fa4",
                                thumbnailUrl = "https://via.placeholder.com/150/121fa4"
                            ),
                            PictureData(
                                albumId = 3,
                                id = 113,
                                title = "hic nulla consectetur",
                                url = "https://via.placeholder.com/600/1dff02",
                                thumbnailUrl = "https://via.placeholder.com/150/1dff02"
                            ),
                            PictureData(
                                albumId = 3,
                                id = 114,
                                title = "consequatur quaerat sunt et",
                                url = "https://via.placeholder.com/600/e79b4e",
                                thumbnailUrl = "https://via.placeholder.com/150/e79b4e"
                            ),
                            PictureData(
                                albumId = 3,
                                id = 115,
                                title = "unde minus molestias",
                                url = "https://via.placeholder.com/600/da7ddf",
                                thumbnailUrl = "https://via.placeholder.com/150/da7ddf"
                            ),
                            PictureData(
                                albumId = 3,
                                id = 116,
                                title = "et iure eius enim explicabo",
                                url = "https://via.placeholder.com/600/aac33b",
                                thumbnailUrl = "https://via.placeholder.com/150/aac33b"
                            ),
                            PictureData(
                                albumId = 3,
                                id = 117,
                                title = "dolore quo nemo omnis odio et iure explicabo",
                                url = "https://via.placeholder.com/600/b2fe8",
                                thumbnailUrl = "https://via.placeholder.com/150/b2fe8"
                            )
                        )
                    )
                ),
                collectedGetResults
            )
        }

    @Test
    fun `should invoke API when fetching pictures and local database is empty`() = runTest {
        // Given
        coEvery { mockDao.getPictures() } returns emptyList()
        coEvery { mockApi.getPictures() } returns emptyList()
        coEvery { mockDao.insert(any()) } returns Unit

        // When
        repository.getPictures().collect()

        // Then
        coVerify(exactly = 1) { mockApi.getPictures() }
        confirmVerified(mockApi)
    }

    @Test
    fun `should emit Progress + Error results when fetching pictures and remote API responds with Http error`() = runTest {
        // Given
        val apiError: HttpException = mockk()
        coEvery { mockDao.getPictures() } returns emptyList()
        coEvery { mockApi.getPictures() } coAnswers { throw apiError }

        // When
        val collectedGetResults = repository.getPictures().toList()

        // Then
        assertEquals(
            listOf(
                FetchResult.Progress,
                FetchResult.Error
            ),
            collectedGetResults
        )
    }

    @Test
    fun `should emit Progress + Error results when fetching pictures and call to remote API fails`() = runTest {
        // Given
        val apiError = IOException()
        coEvery { mockDao.getPictures() } returns emptyList()
        coEvery { mockApi.getPictures() } coAnswers { throw apiError }

        // When
        val collectedGetResults = repository.getPictures().toList()

        // Then
        assertEquals(
            listOf(
                FetchResult.Progress,
                FetchResult.Error
            ),
            collectedGetResults
        )
    }

    @Test
    fun `should emit Progress + Success results with picture list when fetching pictures and remote API successfully responds with pictures`() =
        runTest {
            // Given
            val remotePictures = listOf(
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
            coEvery { mockDao.getPictures() } returns emptyList()
            coEvery { mockApi.getPictures() } returns remotePictures
            coEvery { mockDao.insert(any()) } returns Unit

            // When
            val collectedGetResults = repository.getPictures().toList()

            // Then
            assertEquals(
                listOf(
                    FetchResult.Progress,
                    FetchResult.Success(
                        listOf(
                            PictureData(
                                albumId = 1,
                                id = 1,
                                title = "accusamus beatae ad facilis cum similique qui sunt",
                                url = "https://via.placeholder.com/600/92c952",
                                thumbnailUrl = "https://via.placeholder.com/150/92c952"
                            ),
                            PictureData(
                                albumId = 1,
                                id = 2,
                                title = "reprehenderit est deserunt velit ipsam",
                                url = "https://via.placeholder.com/600/771796",
                                thumbnailUrl = "https://via.placeholder.com/150/771796"
                            ),
                            PictureData(
                                albumId = 1,
                                id = 3,
                                title = "officia porro iure quia iusto qui ipsa ut modi",
                                url = "https://via.placeholder.com/600/24f355",
                                thumbnailUrl = "https://via.placeholder.com/150/24f355"
                            ),
                            PictureData(
                                albumId = 2,
                                id = 51,
                                title = "non sunt voluptatem placeat consequuntur rem incidunt",
                                url = "https://via.placeholder.com/600/8e973b",
                                thumbnailUrl = "https://via.placeholder.com/150/8e973b"
                            ),
                            PictureData(
                                albumId = 2,
                                id = 52,
                                title = "eveniet pariatur quia nobis reiciendis laboriosam ea",
                                url = "https://via.placeholder.com/600/121fa4",
                                thumbnailUrl = "https://via.placeholder.com/150/121fa4"
                            ),
                            PictureData(
                                albumId = 3,
                                id = 113,
                                title = "hic nulla consectetur",
                                url = "https://via.placeholder.com/600/1dff02",
                                thumbnailUrl = "https://via.placeholder.com/150/1dff02"
                            ),
                            PictureData(
                                albumId = 3,
                                id = 114,
                                title = "consequatur quaerat sunt et",
                                url = "https://via.placeholder.com/600/e79b4e",
                                thumbnailUrl = "https://via.placeholder.com/150/e79b4e"
                            ),
                            PictureData(
                                albumId = 3,
                                id = 115,
                                title = "unde minus molestias",
                                url = "https://via.placeholder.com/600/da7ddf",
                                thumbnailUrl = "https://via.placeholder.com/150/da7ddf"
                            ),
                            PictureData(
                                albumId = 3,
                                id = 116,
                                title = "et iure eius enim explicabo",
                                url = "https://via.placeholder.com/600/aac33b",
                                thumbnailUrl = "https://via.placeholder.com/150/aac33b"
                            ),
                            PictureData(
                                albumId = 3,
                                id = 117,
                                title = "dolore quo nemo omnis odio et iure explicabo",
                                url = "https://via.placeholder.com/600/b2fe8",
                                thumbnailUrl = "https://via.placeholder.com/150/b2fe8"
                            )
                        )
                    )
                ),
                collectedGetResults
            )
        }

    @Test
    fun `should emit Progress + Success results with empty picture list when fetching pictures and remote API successfully responds without pictures`() =
        runTest {
            // Given
            coEvery { mockDao.getPictures() } returns emptyList()
            coEvery { mockApi.getPictures() } returns emptyList()
            coEvery { mockDao.insert(any()) } returns Unit

            // When
            val collectedGetResults = repository.getPictures().toList()

            // Then
            assertEquals(
                listOf(
                    FetchResult.Progress,
                    FetchResult.Success(emptyList())
                ),
                collectedGetResults
            )
        }

    @Test
    fun `should store pictures in local database when fetching pictures and remote API successfully responds`() = runTest {
        // Given
        val remotePictures = listOf(
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
        coEvery { mockDao.getPictures() } returns emptyList()
        coEvery { mockApi.getPictures() } returns remotePictures
        coEvery { mockDao.insert(any()) } returns Unit

        // When
        repository.getPictures().collect()

        // Then
        coVerify(exactly = 1) { mockDao.insert(pictures = listOf(
            PictureEntity(
                albumId = 1,
                id = 1,
                title = "accusamus beatae ad facilis cum similique qui sunt",
                url = "https://via.placeholder.com/600/92c952",
                thumbnailUrl = "https://via.placeholder.com/150/92c952"
            ),
            PictureEntity(
                albumId = 1,
                id = 2,
                title = "reprehenderit est deserunt velit ipsam",
                url = "https://via.placeholder.com/600/771796",
                thumbnailUrl = "https://via.placeholder.com/150/771796"
            ),
            PictureEntity(
                albumId = 1,
                id = 3,
                title = "officia porro iure quia iusto qui ipsa ut modi",
                url = "https://via.placeholder.com/600/24f355",
                thumbnailUrl = "https://via.placeholder.com/150/24f355"
            ),
            PictureEntity(
                albumId = 2,
                id = 51,
                title = "non sunt voluptatem placeat consequuntur rem incidunt",
                url = "https://via.placeholder.com/600/8e973b",
                thumbnailUrl = "https://via.placeholder.com/150/8e973b"
            ),
            PictureEntity(
                albumId = 2,
                id = 52,
                title = "eveniet pariatur quia nobis reiciendis laboriosam ea",
                url = "https://via.placeholder.com/600/121fa4",
                thumbnailUrl = "https://via.placeholder.com/150/121fa4"
            ),
            PictureEntity(
                albumId = 3,
                id = 113,
                title = "hic nulla consectetur",
                url = "https://via.placeholder.com/600/1dff02",
                thumbnailUrl = "https://via.placeholder.com/150/1dff02"
            ),
            PictureEntity(
                albumId = 3,
                id = 114,
                title = "consequatur quaerat sunt et",
                url = "https://via.placeholder.com/600/e79b4e",
                thumbnailUrl = "https://via.placeholder.com/150/e79b4e"
            ),
            PictureEntity(
                albumId = 3,
                id = 115,
                title = "unde minus molestias",
                url = "https://via.placeholder.com/600/da7ddf",
                thumbnailUrl = "https://via.placeholder.com/150/da7ddf"
            ),
            PictureEntity(
                albumId = 3,
                id = 116,
                title = "et iure eius enim explicabo",
                url = "https://via.placeholder.com/600/aac33b",
                thumbnailUrl = "https://via.placeholder.com/150/aac33b"
            ),
            PictureEntity(
                albumId = 3,
                id = 117,
                title = "dolore quo nemo omnis odio et iure explicabo",
                url = "https://via.placeholder.com/600/b2fe8",
                thumbnailUrl = "https://via.placeholder.com/150/b2fe8"
            )
        )) }
    }
}