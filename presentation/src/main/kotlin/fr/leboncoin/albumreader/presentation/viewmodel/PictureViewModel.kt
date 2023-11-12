package fr.leboncoin.albumreader.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.albumreader.common.core.stateInForAndroidLifecycle
import fr.leboncoin.albumreader.domain.interactor.GetPictureDetailsInteractor
import fr.leboncoin.albumreader.presentation.R
import fr.leboncoin.albumreader.presentation.model.PictureUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class PictureViewModel @Inject constructor(
    private val getPictureDetailsInteractor: GetPictureDetailsInteractor
) : ViewModel() {

    private val pictureIdState: MutableStateFlow<Int?> = MutableStateFlow(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<PictureUiState> = pictureIdState
        .filterNotNull()
        .flatMapLatest { pictureId ->
            getPictureDetailsInteractor(pictureId = pictureId)
        }
        .map { result -> result.toUiState() }
        .catch { throwable ->
            Timber.d(throwable, "Error while fetching picture ${pictureIdState.value}")
            emit(PictureUiState.Error(R.string.error_message_generic))
        }
        .stateInForAndroidLifecycle(scope = viewModelScope, initialValue = PictureUiState.Loading)

    fun getPictureDetails(pictureId: Int) {
        pictureIdState.update { pictureId }
    }

    private fun GetPictureDetailsInteractor.Result.toUiState(): PictureUiState = when (this) {
        is GetPictureDetailsInteractor.Result.Progress -> PictureUiState.Loading
        is GetPictureDetailsInteractor.Result.Error -> PictureUiState.Error(R.string.error_message_generic)
        is GetPictureDetailsInteractor.Result.Success -> PictureUiState.Success(
            title = details.title,
            pictureUrl = details.url
        )
    }
}