package fr.leboncoin.albumreader.presentation.model

import androidx.annotation.StringRes

internal sealed interface PictureUiState {
    data object Loading : PictureUiState

    data class Error(@StringRes val errorMessageResId: Int) : PictureUiState

    data class Success(val title: String, val pictureUrl: String) : PictureUiState
}