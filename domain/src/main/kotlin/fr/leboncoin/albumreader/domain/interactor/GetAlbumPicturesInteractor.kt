package fr.leboncoin.albumreader.domain.interactor

import fr.leboncoin.albumreader.domain.model.Picture
import fr.leboncoin.albumreader.domain.model.PictureData
import fr.leboncoin.albumreader.domain.repository.PicturesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAlbumPicturesInteractor @Inject constructor(
    private val picturesRepository: PicturesRepository
) {

    sealed interface Result {
        data object Progress : Result
        data object Error : Result
        data class Success(val pictures: List<Picture>) : Result
    }

    operator fun invoke(albumId: Int): Flow<Result> = picturesRepository.getPictures()
        .map { result ->
            when (result) {
                is PicturesRepository.FetchResult.Error -> Result.Error
                is PicturesRepository.FetchResult.Progress -> Result.Progress
                is PicturesRepository.FetchResult.Success -> {
                    val albumPictures = result.pictures.filterByAlbumId(albumId = albumId)
                    when (albumPictures.isEmpty()) {
                        true -> Result.Error
                        false -> Result.Success(pictures = albumPictures)
                    }
                }
            }
        }

    private fun List<PictureData>.filterByAlbumId(albumId: Int): List<Picture> =
        filter { picture -> picture.albumId == albumId }
            .map { picture -> picture.toDomain() }

    private fun PictureData.toDomain(): Picture = Picture(
        id = id,
        thumbnailUrl = thumbnailUrl
    )
}