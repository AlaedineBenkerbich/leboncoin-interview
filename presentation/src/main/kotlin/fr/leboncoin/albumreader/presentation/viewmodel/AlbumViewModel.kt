package fr.leboncoin.albumreader.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.albumreader.common.core.stateInForAndroidLifecycle
import fr.leboncoin.albumreader.domain.interactor.GetAlbumPicturesInteractor
import fr.leboncoin.albumreader.domain.model.Picture
import fr.leboncoin.albumreader.presentation.R
import fr.leboncoin.albumreader.presentation.model.AlbumUiState
import fr.leboncoin.albumreader.presentation.model.ItemUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class AlbumViewModel @Inject constructor(
    private val getAlbumPicturesInteractor: GetAlbumPicturesInteractor
) : ViewModel() {

    private val albumIdState: MutableStateFlow<Int?> = MutableStateFlow(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<AlbumUiState> = albumIdState
        .filterNotNull()
        .flatMapLatest { albumId ->
            getAlbumPicturesInteractor(albumId = albumId)
        }
        .map { result -> result.toUiState() }
        .catch { throwable ->
            Timber.d(throwable, "Error while fetching pictures of album ${albumIdState.value}")
            emit(AlbumUiState.Error(R.string.error_message_generic))
        }
        .stateInForAndroidLifecycle(scope = viewModelScope, initialValue = AlbumUiState.Loading)

    val pictureSelectionState: SharedFlow<Int> get() = _pictureSelectionState
    private val _pictureSelectionState = MutableSharedFlow<Int>()
    
    fun getAlbumPictures(albumId: Int) {
        albumIdState.update { albumId }
    }

    @VisibleForTesting
    internal fun onPictureSelection(id: Int) {
        viewModelScope.launch {
            _pictureSelectionState.emit(id)
        }
    }

    private fun GetAlbumPicturesInteractor.Result.toUiState(): AlbumUiState = when (this) {
        is GetAlbumPicturesInteractor.Result.Progress -> AlbumUiState.Loading
        is GetAlbumPicturesInteractor.Result.Error -> AlbumUiState.Error(R.string.error_message_generic)
        is GetAlbumPicturesInteractor.Result.Success -> AlbumUiState.Success(
            pictures = pictures.map { picture -> picture.toUiModel() }
        )
    }

    private fun Picture.toUiModel(): ItemUiModel =
        ItemUiModel(
            id = id,
            thumbnailUrl = thumbnailUrl,
            onSelection = ::onPictureSelection
        )
}