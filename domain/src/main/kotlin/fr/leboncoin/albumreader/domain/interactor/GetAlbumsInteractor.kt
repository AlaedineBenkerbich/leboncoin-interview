package fr.leboncoin.albumreader.domain.interactor

import fr.leboncoin.albumreader.domain.model.Album
import fr.leboncoin.albumreader.domain.model.PictureData
import fr.leboncoin.albumreader.domain.repository.PicturesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAlbumsInteractor @Inject constructor(
    private val picturesRepository: PicturesRepository
) {

    sealed interface Result {
        data object Progress : Result
        data object Error : Result
        data class Success(val albums: List<Album>) : Result
    }

    operator fun invoke(): Flow<Result> = picturesRepository.getPictures()
        .map { result ->
            when (result) {
                is PicturesRepository.FetchResult.Error -> Result.Error
                is PicturesRepository.FetchResult.Progress -> Result.Progress
                is PicturesRepository.FetchResult.Success -> Result.Success(
                    albums = result.pictures.groupByAlbum()
                )
            }
        }

    private fun List<PictureData>.groupByAlbum(): List<Album> = groupBy(PictureData::albumId)
        .map { (albumId, pictures) ->
            Album(
                id = albumId,
                thumbnailUrl = pictures.first().thumbnailUrl
            )
        }
}