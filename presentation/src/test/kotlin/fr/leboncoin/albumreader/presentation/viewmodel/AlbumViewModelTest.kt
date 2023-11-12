package fr.leboncoin.albumreader.presentation.viewmodel

import fr.leboncoin.albumreader.common.testingtools.CoroutineDispatcherRule
import fr.leboncoin.albumreader.domain.interactor.GetAlbumPicturesInteractor
import fr.leboncoin.albumreader.domain.model.Picture
import fr.leboncoin.albumreader.presentation.R
import fr.leboncoin.albumreader.presentation.model.AlbumUiState
import fr.leboncoin.albumreader.presentation.model.ItemUiModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Unit tests of [AlbumViewModel].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AlbumViewModelTest {

    @get:Rule
    val coroutineDispatcherRule = CoroutineDispatcherRule(StandardTestDispatcher())

    private val mockGetAlbumPicturesInteractor: GetAlbumPicturesInteractor = mockk()

    private val viewModel: AlbumViewModel by lazy {
        AlbumViewModel(mockGetAlbumPicturesInteractor)
    }
    
    // region ViewModel initialization

    @Test
    fun `should emit Loading state when initializing the view model`() =
        runTest {
            // Given
            val collected = mutableListOf<AlbumUiState>()

            // When
            val job = launch {
                viewModel.state.collect(collected::add)
            }
            advanceUntilIdle()

            // Then
            assertEquals(
                listOf<AlbumUiState>(AlbumUiState.Loading),
                collected
            )
            job.cancel()
        }
    
    // endregion

    // region `getAlbumPictures`

    @Test
    fun `should invoke the interactor when getAlbumPictures is called`() = runTest {
        // Given
        every { mockGetAlbumPicturesInteractor(albumId = 2) } returns emptyFlow()

        // When
        val job = launch(StandardTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        viewModel.getAlbumPictures(albumId = 2)
        advanceUntilIdle()

        // Then
        verify(exactly = 1) { mockGetAlbumPicturesInteractor(albumId = 2) }
        confirmVerified(mockGetAlbumPicturesInteractor)
        job.cancel()
    }

    @Test
    fun `should emit Loading state when getAlbumPictures is called and interactor returns Progress`() =
        runTest {
            // Given
            val collected = mutableListOf<AlbumUiState>()
            every { mockGetAlbumPicturesInteractor(albumId = 2) } returns flowOf(
                GetAlbumPicturesInteractor.Result.Progress
            )

            // When
            val job = launch {
                viewModel.state.collect(collected::add)
            }
            viewModel.getAlbumPictures(albumId = 2)
            advanceUntilIdle()

            // Then
            assertEquals(
                listOf<AlbumUiState>(AlbumUiState.Loading),
                collected
            )
            job.cancel()
        }

    @Test
    fun `should emit Loading + Error states when getAlbumPictures is called and interactor returns Error`() =
        runTest {
            // Given
            val collected = mutableListOf<AlbumUiState>()
            every { mockGetAlbumPicturesInteractor(albumId = 2) } returns flowOf(
                GetAlbumPicturesInteractor.Result.Error
            )

            // When
            val job = launch {
                viewModel.state.collect(collected::add)
            }
            viewModel.getAlbumPictures(albumId = 2)
            advanceUntilIdle()

            // Then
            assertEquals(
                listOf(
                    AlbumUiState.Loading,
                    AlbumUiState.Error(R.string.error_message_generic)
                ),
                collected
            )
            job.cancel()
        }

    @Test
    fun `should emit Loading + Error states when getAlbumPictures is called and an exception is thrown by the interactor`() =
        runTest {
            // Given
            val collected = mutableListOf<AlbumUiState>()
            every { mockGetAlbumPicturesInteractor(albumId = 2) } returns flow {
                throw Exception("Fake exception")
            }

            // When
            val job = launch {
                viewModel.state.collect(collected::add)
            }
            viewModel.getAlbumPictures(albumId = 2)
            advanceUntilIdle()

            // Then
            assertEquals(
                listOf(
                    AlbumUiState.Loading,
                    AlbumUiState.Error(R.string.error_message_generic)
                ),
                collected
            )
            job.cancel()
        }

    @Test
    fun `should emit Success state when getAlbumPictures is called and interactor returns Success`() =
        runTest {
            // Given
            val collected = mutableListOf<AlbumUiState>()
            every { mockGetAlbumPicturesInteractor(albumId = 2) } returns flowOf(
                GetAlbumPicturesInteractor.Result.Success(
                    pictures = listOf(
                        Picture(
                            id = 10,
                            thumbnailUrl = "https://via.placeholder.com/150/92c952"
                        ),
                        Picture(
                            id = 11,
                            thumbnailUrl = "https://via.placeholder.com/150/8e973b"
                        ),
                        Picture(
                            id = 12,
                            thumbnailUrl = "https://via.placeholder.com/150/1dff02"
                        )
                    )
                )
            )

            // When
            val job = launch {
                viewModel.state.collect(collected::add)
            }
            viewModel.getAlbumPictures(albumId = 2)
            advanceUntilIdle()

            // Then
            assertEquals(
                listOf(
                    AlbumUiState.Loading,
                    AlbumUiState.Success(
                        pictures = listOf(
                            ItemUiModel(
                                id = 10,
                                thumbnailUrl = "https://via.placeholder.com/150/92c952",
                                onSelection = viewModel::onPictureSelection
                            ),
                            ItemUiModel(
                                id = 11,
                                thumbnailUrl = "https://via.placeholder.com/150/8e973b",
                                onSelection = viewModel::onPictureSelection
                            ),
                            ItemUiModel(
                                id = 12,
                                thumbnailUrl = "https://via.placeholder.com/150/1dff02",
                                onSelection = viewModel::onPictureSelection
                            )
                        )
                    )
                ),
                collected
            )
            job.cancel()
        }

    // endregion

    // region `onPictureSelection`

    @Test
    fun `should emit picture id when onPictureSelection is called`() = runTest {
        // Given
        val collected = mutableListOf<Int>()
        every { mockGetAlbumPicturesInteractor(albumId = 2) } returns flowOf(
            GetAlbumPicturesInteractor.Result.Success(
                pictures = listOf(
                    Picture(
                        id = 10,
                        thumbnailUrl = "https://via.placeholder.com/150/92c952"
                    ),
                    Picture(
                        id = 11,
                        thumbnailUrl = "https://via.placeholder.com/150/8e973b"
                    ),
                    Picture(
                        id = 12,
                        thumbnailUrl = "https://via.placeholder.com/150/1dff02"
                    )
                )
            )
        )

        // When
        val job = launch {
            viewModel.pictureSelectionState.collect(collected::add)
        }
        viewModel.getAlbumPictures(albumId = 2)
        advanceUntilIdle()
        viewModel.onPictureSelection(id = 11)
        advanceUntilIdle()

        // Then
        assertEquals(
            listOf(11),
            collected
        )
        job.cancel()
    }

    // endregion
}