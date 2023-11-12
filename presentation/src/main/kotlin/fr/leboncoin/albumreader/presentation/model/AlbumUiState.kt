package fr.leboncoin.albumreader.presentation.model

import androidx.annotation.StringRes

internal sealed interface AlbumUiState {
    data object Loading : AlbumUiState

    data class Error(@StringRes val errorMessageResId: Int) : AlbumUiState

    data class Success(val pictures: List<ItemUiModel>) : AlbumUiState
}