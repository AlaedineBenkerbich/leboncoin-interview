package fr.leboncoin.albumreader.data

import fr.leboncoin.albumreader.api.Api
import fr.leboncoin.albumreader.api.RemotePicture
import fr.leboncoin.albumreader.domain.model.Picture
import fr.leboncoin.albumreader.domain.repository.PicturesRepository.FetchResult
import io.mockk.coEvery
import io.mockk.coVerify
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
    private val mockApi = mockk<Api>()

    private val repository: PicturesRepositoryImpl by lazy {
        PicturesRepositoryImpl(
            mockApi,
            UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `should invoke API when fetching pictures`() = runTest {
        // Given
        coEvery { mockApi.getPictures() } returns emptyList()

        // When
        repository.getPictures().collect()

        // Then
        coVerify(exactly = 1) { mockApi.getPictures() }
    }

    @Test
    fun `should emit Progress + Error results when fetching pictures and remote API responds with Http error`() = runTest {
        // Given
        val apiError: HttpException = mockk()
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
            coEvery { mockApi.getPictures() } returns remotePictures

            // When
            val collectedGetResults = repository.getPictures().toList()

            // Then
            assertEquals(
                listOf(
                    FetchResult.Progress,
                    FetchResult.Success(
                        listOf(
                            Picture(
                                albumId = 1,
                                id = 1,
                                title = "accusamus beatae ad facilis cum similique qui sunt",
                                url = "https://via.placeholder.com/600/92c952",
                                thumbnailUrl = "https://via.placeholder.com/150/92c952"
                            ),
                            Picture(
                                albumId = 1,
                                id = 2,
                                title = "reprehenderit est deserunt velit ipsam",
                                url = "https://via.placeholder.com/600/771796",
                                thumbnailUrl = "https://via.placeholder.com/150/771796"
                            ),
                            Picture(
                                albumId = 1,
                                id = 3,
                                title = "officia porro iure quia iusto qui ipsa ut modi",
                                url = "https://via.placeholder.com/600/24f355",
                                thumbnailUrl = "https://via.placeholder.com/150/24f355"
                            ),
                            Picture(
                                albumId = 2,
                                id = 51,
                                title = "non sunt voluptatem placeat consequuntur rem incidunt",
                                url = "https://via.placeholder.com/600/8e973b",
                                thumbnailUrl = "https://via.placeholder.com/150/8e973b"
                            ),
                            Picture(
                                albumId = 2,
                                id = 52,
                                title = "eveniet pariatur quia nobis reiciendis laboriosam ea",
                                url = "https://via.placeholder.com/600/121fa4",
                                thumbnailUrl = "https://via.placeholder.com/150/121fa4"
                            ),
                            Picture(
                                albumId = 3,
                                id = 113,
                                title = "hic nulla consectetur",
                                url = "https://via.placeholder.com/600/1dff02",
                                thumbnailUrl = "https://via.placeholder.com/150/1dff02"
                            ),
                            Picture(
                                albumId = 3,
                                id = 114,
                                title = "consequatur quaerat sunt et",
                                url = "https://via.placeholder.com/600/e79b4e",
                                thumbnailUrl = "https://via.placeholder.com/150/e79b4e"
                            ),
                            Picture(
                                albumId = 3,
                                id = 115,
                                title = "unde minus molestias",
                                url = "https://via.placeholder.com/600/da7ddf",
                                thumbnailUrl = "https://via.placeholder.com/150/da7ddf"
                            ),
                            Picture(
                                albumId = 3,
                                id = 116,
                                title = "et iure eius enim explicabo",
                                url = "https://via.placeholder.com/600/aac33b",
                                thumbnailUrl = "https://via.placeholder.com/150/aac33b"
                            ),
                            Picture(
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
            coEvery { mockApi.getPictures() } returns emptyList()

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
}