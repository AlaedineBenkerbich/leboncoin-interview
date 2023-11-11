package fr.leboncoin.albumreader.domain.repository

import fr.leboncoin.albumreader.domain.model.PictureData
import kotlinx.coroutines.flow.Flow

interface PicturesRepository {

    fun getPictures(): Flow<FetchResult>

    sealed interface FetchResult {
        data object Progress : FetchResult
        data class Success(val pictures: List<PictureData>) : FetchResult
        data object Error : FetchResult
    }
}