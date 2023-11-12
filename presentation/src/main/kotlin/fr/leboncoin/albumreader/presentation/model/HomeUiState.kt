package fr.leboncoin.albumreader.presentation.model

import androidx.annotation.StringRes

internal sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Error(@StringRes val errorMessageResId: Int) : HomeUiState

    data class Success(val albums: List<ItemUiModel>) : HomeUiState
}