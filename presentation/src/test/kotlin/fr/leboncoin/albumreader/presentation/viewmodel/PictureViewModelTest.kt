package fr.leboncoin.albumreader.presentation.viewmodel

import fr.leboncoin.albumreader.common.testingtools.CoroutineDispatcherRule
import fr.leboncoin.albumreader.domain.interactor.GetPictureDetailsInteractor
import fr.leboncoin.albumreader.domain.model.PictureDetails
import fr.leboncoin.albumreader.presentation.R
import fr.leboncoin.albumreader.presentation.model.PictureUiState
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
 * Unit tests of [PictureViewModel].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PictureViewModelTest {

    @get:Rule
    val coroutineDispatcherRule = CoroutineDispatcherRule(StandardTestDispatcher())

    private val mockGetPictureDetailsInteractor: GetPictureDetailsInteractor = mockk()

    private val viewModel: PictureViewModel by lazy {
        PictureViewModel(mockGetPictureDetailsInteractor)
    }

    // region ViewModel initialization

    @Test
    fun `should emit Loading state when initializing the view model`() =
        runTest {
            // Given
            val collected = mutableListOf<PictureUiState>()

            // When
            val job = launch {
                viewModel.state.collect(collected::add)
            }
            advanceUntilIdle()

            // Then
            assertEquals(
                listOf<PictureUiState>(PictureUiState.Loading),
                collected
            )
            job.cancel()
        }

    // endregion

    // region `getPictureDetails`

    @Test
    fun `should invoke the interactor when getPictureDetails is called`() = runTest {
        // Given
        every { mockGetPictureDetailsInteractor(pictureId = 11) } returns emptyFlow()

        // When
        val job = launch(StandardTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        viewModel.getPictureDetails(pictureId = 11)
        advanceUntilIdle()

        // Then
        verify(exactly = 1) { mockGetPictureDetailsInteractor(pictureId = 11) }
        confirmVerified(mockGetPictureDetailsInteractor)
        job.cancel()
    }

    @Test
    fun `should emit Loading state when getPictureDetails is called and interactor returns Progress`() =
        runTest {
            // Given
            val collected = mutableListOf<PictureUiState>()
            every { mockGetPictureDetailsInteractor(pictureId = 11) } returns flowOf(
                GetPictureDetailsInteractor.Result.Progress
            )

            // When
            val job = launch {
                viewModel.state.collect(collected::add)
            }
            viewModel.getPictureDetails(pictureId = 11)
            advanceUntilIdle()

            // Then
            assertEquals(
                listOf<PictureUiState>(PictureUiState.Loading),
                collected
            )
            job.cancel()
        }

    @Test
    fun `should emit Loading + Error states when getPictureDetails is called and interactor returns Error`() =
        runTest {
            // Given
            val collected = mutableListOf<PictureUiState>()
            every { mockGetPictureDetailsInteractor(pictureId = 11) } returns flowOf(
                GetPictureDetailsInteractor.Result.Error
            )

            // When
            val job = launch {
                viewModel.state.collect(collected::add)
            }
            viewModel.getPictureDetails(pictureId = 11)
            advanceUntilIdle()

            // Then
            assertEquals(
                listOf(
                    PictureUiState.Loading,
                    PictureUiState.Error(R.string.error_message_generic)
                ),
                collected
            )
            job.cancel()
        }

    @Test
    fun `should emit Loading + Error states when getPictureDetails is called and an exception is thrown by the interactor`() =
        runTest {
            // Given
            val collected = mutableListOf<PictureUiState>()
            every { mockGetPictureDetailsInteractor(pictureId = 11) } returns flow {
                throw Exception("Fake exception")
            }

            // When
            val job = launch {
                viewModel.state.collect(collected::add)
            }
            viewModel.getPictureDetails(pictureId = 11)
            advanceUntilIdle()

            // Then
            assertEquals(
                listOf(
                    PictureUiState.Loading,
                    PictureUiState.Error(R.string.error_message_generic)
                ),
                collected
            )
            job.cancel()
        }

    @Test
    fun `should emit Success state when getPictureDetails is called and interactor returns Success`() =
        runTest {
            // Given
            val collected = mutableListOf<PictureUiState>()
            every { mockGetPictureDetailsInteractor(pictureId = 11) } returns flowOf(
                GetPictureDetailsInteractor.Result.Success(
                    details = PictureDetails(
                        title = "eveniet pariatur quia nobis reiciendis laboriosam ea",
                        url = "https://via.placeholder.com/600/121fa4"
                    )
                )
            )

            // When
            val job = launch {
                viewModel.state.collect(collected::add)
            }
            viewModel.getPictureDetails(pictureId = 11)
            advanceUntilIdle()

            // Then
            assertEquals(
                listOf(
                    PictureUiState.Loading,
                    PictureUiState.Success(
                        title = "eveniet pariatur quia nobis reiciendis laboriosam ea",
                        pictureUrl = "https://via.placeholder.com/600/121fa4"
                    )
                ),
                collected
            )
            job.cancel()
        }

    // endregion
}