package fr.leboncoin.albumreader.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.albumreader.common.core.stateInForAndroidLifecycle
import fr.leboncoin.albumreader.domain.interactor.GetAlbumsInteractor
import fr.leboncoin.albumreader.domain.model.Album
import fr.leboncoin.albumreader.presentation.R
import fr.leboncoin.albumreader.presentation.model.HomeUiState
import fr.leboncoin.albumreader.presentation.model.ItemUiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    getAlbumsInteractor: GetAlbumsInteractor
) : ViewModel() {

    val state: StateFlow<HomeUiState> =
        getAlbumsInteractor()
            .map { result -> result.toUiState() }
            .catch { throwable ->
                Timber.d(throwable, "Error while fetching albums")
                emit(HomeUiState.Error(R.string.error_message_generic))
            }
            .stateInForAndroidLifecycle(scope = viewModelScope, initialValue = HomeUiState.Loading)

    val albumSelectionState: SharedFlow<Int> get() = _albumSelectionState
    private val _albumSelectionState = MutableSharedFlow<Int>()

    private fun GetAlbumsInteractor.Result.toUiState(): HomeUiState = when (this) {
        is GetAlbumsInteractor.Result.Progress -> HomeUiState.Loading
        is GetAlbumsInteractor.Result.Error -> HomeUiState.Error(R.string.error_message_generic)
        is GetAlbumsInteractor.Result.Success -> HomeUiState.Success(
            albums = albums.map { album -> album.toUiModel() }
        )
    }

    private fun Album.toUiModel(): ItemUiModel =
        ItemUiModel(
            id = id,
            thumbnailUrl = thumbnailUrl,
            onSelection = ::onAlbumSelection
        )

    @VisibleForTesting
    internal fun onAlbumSelection(id: Int) {
        viewModelScope.launch {
            _albumSelectionState.emit(id)
        }
    }
}