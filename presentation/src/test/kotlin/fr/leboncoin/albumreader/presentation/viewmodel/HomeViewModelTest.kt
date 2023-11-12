package fr.leboncoin.albumreader.presentation.viewmodel

import fr.leboncoin.albumreader.common.testingtools.CoroutineDispatcherRule
import fr.leboncoin.albumreader.domain.interactor.GetAlbumsInteractor
import fr.leboncoin.albumreader.domain.model.Album
import fr.leboncoin.albumreader.presentation.R
import fr.leboncoin.albumreader.presentation.model.HomeUiState
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
 * Unit tests of [HomeViewModel].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val coroutineDispatcherRule = CoroutineDispatcherRule(StandardTestDispatcher())

    private val mockGetAlbumsInteractor: GetAlbumsInteractor = mockk()

    private val viewModel: HomeViewModel by lazy {
        HomeViewModel(mockGetAlbumsInteractor)
    }

    // region ViewModel initialization

    @Test
    fun `should invoke the interactor on ViewModel initialization`() = runTest {
        // Given
        every { mockGetAlbumsInteractor() } returns emptyFlow()

        // When
        val job = launch(StandardTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        advanceUntilIdle()

        // Then
        verify(exactly = 1) { mockGetAlbumsInteractor() }
        confirmVerified(mockGetAlbumsInteractor)
        job.cancel()
    }

    @Test
    fun `should emit Loading state when initializing the view model and interactor returns Progress`() =
        runTest {
            // Given
            val collected = mutableListOf<HomeUiState>()
            every { mockGetAlbumsInteractor() } returns flowOf(
                GetAlbumsInteractor.Result.Progress
            )

            // When
            val job = launch {
                viewModel.state.collect(collected::add)
            }
            advanceUntilIdle()

            // Then
            assertEquals(
                listOf<HomeUiState>(HomeUiState.Loading),
                collected
            )
            job.cancel()
        }

    @Test
    fun `should emit Loading + Error states when initializing the view model and interactor returns Error`() =
        runTest {
            // Given
            val collected = mutableListOf<HomeUiState>()
            every { mockGetAlbumsInteractor() } returns flowOf(
                GetAlbumsInteractor.Result.Error
            )

            // When
            val job = launch {
                viewModel.state.collect(collected::add)
            }
            advanceUntilIdle()

            // Then
            assertEquals(
                listOf(
                    HomeUiState.Loading,
                    HomeUiState.Error(R.string.error_message_generic)
                ),
                collected
            )
            job.cancel()
        }

    @Test
    fun `should emit Loading + Error states when initializing the view model and an exception is thrown by the interactor`() =
        runTest {
            // Given
            val collected = mutableListOf<HomeUiState>()
            every { mockGetAlbumsInteractor() } returns flow {
                throw Exception("Fake exception")
            }

            // When
            val job = launch {
                viewModel.state.collect(collected::add)
            }
            advanceUntilIdle()

            // Then
            assertEquals(
                listOf(
                    HomeUiState.Loading,
                    HomeUiState.Error(R.string.error_message_generic)
                ),
                collected
            )
            job.cancel()
        }

    @Test
    fun `should emit Success state when initializing the view model and interactor returns Success`() =
        runTest {
            // Given
            val collected = mutableListOf<HomeUiState>()
            every { mockGetAlbumsInteractor() } returns flowOf(
                GetAlbumsInteractor.Result.Success(
                    albums = listOf(
                        Album(
                            id = 1,
                            thumbnailUrl = "https://via.placeholder.com/150/92c952"
                        ),
                        Album(
                            id = 2,
                            thumbnailUrl = "https://via.placeholder.com/150/8e973b"
                        ),
                        Album(
                            id = 3,
                            thumbnailUrl = "https://via.placeholder.com/150/1dff02"
                        )
                    )
                )
            )

            // When
            val job = launch {
                viewModel.state.collect(collected::add)
            }
            advanceUntilIdle()

            // Then
            assertEquals(
                listOf(
                    HomeUiState.Loading,
                    HomeUiState.Success(
                        albums = listOf(
                            ItemUiModel(
                                id = 1,
                                thumbnailUrl = "https://via.placeholder.com/150/92c952",
                                onSelection = viewModel::onAlbumSelection
                            ),
                            ItemUiModel(
                                id = 2,
                                thumbnailUrl = "https://via.placeholder.com/150/8e973b",
                                onSelection = viewModel::onAlbumSelection
                            ),
                            ItemUiModel(
                                id = 3,
                                thumbnailUrl = "https://via.placeholder.com/150/1dff02",
                                onSelection = viewModel::onAlbumSelection
                            )
                        )
                    )
                ),
                collected
            )
            job.cancel()
        }

    // endregion

    // region `onAlbumSelection`

    @Test
    fun `should emit album id when onAlbumSelection is called`() = runTest {
        // Given
        val collected = mutableListOf<Int>()
        every { mockGetAlbumsInteractor() } returns flowOf(
            GetAlbumsInteractor.Result.Success(
                albums = listOf(
                    Album(
                        id = 1,
                        thumbnailUrl = "https://via.placeholder.com/150/92c952"
                    ),
                    Album(
                        id = 2,
                        thumbnailUrl = "https://via.placeholder.com/150/8e973b"
                    ),
                    Album(
                        id = 3,
                        thumbnailUrl = "https://via.placeholder.com/150/1dff02"
                    )
                )
            )
        )

        // When
        val job = launch {
            viewModel.albumSelectionState.collect(collected::add)
        }
        viewModel.onAlbumSelection(id = 2)
        advanceUntilIdle()

        // Then
        assertEquals(
            listOf(2),
            collected
        )
        job.cancel()
    }

    // endregion
}