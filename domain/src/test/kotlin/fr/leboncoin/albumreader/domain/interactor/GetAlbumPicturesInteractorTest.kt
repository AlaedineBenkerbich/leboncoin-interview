package fr.leboncoin.albumreader.domain.interactor

import fr.leboncoin.albumreader.domain.model.Picture
import fr.leboncoin.albumreader.domain.model.PictureData
import fr.leboncoin.albumreader.domain.repository.PicturesRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Unit tests of [GetAlbumPicturesInteractor].
 */
class GetAlbumPicturesInteractorTest {

    private val mockPicturesRepository: PicturesRepository = mockk()
    private val interactor: GetAlbumPicturesInteractor by lazy {
        GetAlbumPicturesInteractor(mockPicturesRepository)
    }

    @Test
    fun `should invoke repository when invoking interactor`() = runTest {
        // Given
        every { mockPicturesRepository.getPictures() } returns emptyFlow()

        // When
        interactor(albumId = 1).collect()

        // Then
        verify(exactly = 1) { mockPicturesRepository.getPictures() }
        confirmVerified(mockPicturesRepository)
    }

    @Test
    fun `should emit Progress result when repository emits Progress`() = runTest {
        // Given
        every { mockPicturesRepository.getPictures() } returns flowOf(
            PicturesRepository.FetchResult.Progress
        )

        // When
        val result = interactor(albumId = 1).toList()

        // Then
        assertEquals(
            listOf<GetAlbumPicturesInteractor.Result>(GetAlbumPicturesInteractor.Result.Progress),
            result
        )
    }

    @Test
    fun `should emit Error result when repository emits Error`() = runTest {
        // Given
        every { mockPicturesRepository.getPictures() } returns flowOf(
            PicturesRepository.FetchResult.Error
        )

        // When
        val result = interactor(albumId = 1).toList()

        // Then
        assertEquals(
            listOf<GetAlbumPicturesInteractor.Result>(GetAlbumPicturesInteractor.Result.Error),
            result
        )
    }

    @Test
    fun `should emit Success result with album pictures when repository emits Success with pictures and album exists`() =
        runTest {
            // Given
            val pictures = listOf(
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
            every { mockPicturesRepository.getPictures() } returns flowOf(
                PicturesRepository.FetchResult.Success(
                    pictures = pictures
                )
            )

            // When
            val result = interactor(albumId = 1).toList()

            // Then
            assertEquals(
                listOf<GetAlbumPicturesInteractor.Result>(
                    GetAlbumPicturesInteractor.Result.Success(
                        pictures = listOf(
                            Picture(
                                id = 1,
                                thumbnailUrl = "https://via.placeholder.com/150/92c952"
                            ),
                            Picture(
                                id = 2,
                                thumbnailUrl = "https://via.placeholder.com/150/771796"
                            ),
                            Picture(
                                id = 3,
                                thumbnailUrl = "https://via.placeholder.com/150/24f355"
                            )
                        )
                    )
                ),
                result
            )
        }

    @Test
    fun `should emit Error result when repository emits Success without pictures`() =
        runTest {
            // Given
            every { mockPicturesRepository.getPictures() } returns flowOf(
                PicturesRepository.FetchResult.Success(
                    pictures = emptyList()
                )
            )

            // When
            val result = interactor(albumId = 1).toList()

            // Then
            assertEquals(
                listOf<GetAlbumPicturesInteractor.Result>(GetAlbumPicturesInteractor.Result.Error),
                result
            )
        }

    @Test
    fun `should emit Error result when repository emits Success with pictures and album doesn't exist`() =
        runTest {
            // Given
            val pictures = listOf(
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
            every { mockPicturesRepository.getPictures() } returns flowOf(
                PicturesRepository.FetchResult.Success(
                    pictures = pictures
                )
            )

            // When
            val result = interactor(albumId = 4).toList()

            // Then
            assertEquals(
                listOf<GetAlbumPicturesInteractor.Result>(GetAlbumPicturesInteractor.Result.Error),
                result
            )
        }
}