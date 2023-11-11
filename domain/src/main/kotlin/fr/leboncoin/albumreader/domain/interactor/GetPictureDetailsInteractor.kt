package fr.leboncoin.albumreader.domain.interactor

import fr.leboncoin.albumreader.domain.model.PictureData
import fr.leboncoin.albumreader.domain.model.PictureDetails
import fr.leboncoin.albumreader.domain.repository.PicturesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPictureDetailsInteractor @Inject constructor(
    private val picturesRepository: PicturesRepository
) {

    sealed interface Result {
        data object Progress : Result
        data object Error : Result
        data class Success(val details: PictureDetails) : Result
    }

    operator fun invoke(pictureId: Int): Flow<Result> = picturesRepository.getPictures()
        .map { result ->
            when (result) {
                is PicturesRepository.FetchResult.Error -> Result.Error
                is PicturesRepository.FetchResult.Progress -> Result.Progress
                is PicturesRepository.FetchResult.Success -> {
                    when (val picture = result.pictures.findById(id = pictureId)?.toDomain()) {
                        null -> Result.Error
                        else -> Result.Success(details = picture)
                    }
                }
            }
        }

    private fun List<PictureData>.findById(id: Int): PictureData? =
        firstOrNull { picture -> picture.id == id }

    private fun PictureData.toDomain(): PictureDetails = PictureDetails(
        title = title,
        url = url
    )
}