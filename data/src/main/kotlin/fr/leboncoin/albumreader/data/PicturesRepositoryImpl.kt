package fr.leboncoin.albumreader.data

import fr.leboncoin.albumreader.api.Api
import fr.leboncoin.albumreader.api.RemotePicture
import fr.leboncoin.albumreader.common.core.DefaultDispatcher
import fr.leboncoin.albumreader.domain.model.Picture
import fr.leboncoin.albumreader.domain.repository.PicturesRepository
import fr.leboncoin.albumreader.domain.repository.PicturesRepository.FetchResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import java.util.logging.Logger
import javax.inject.Inject

class PicturesRepositoryImpl @Inject constructor(
    private val api: Api,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : PicturesRepository {
    companion object {
        private val logger = Logger.getLogger(PicturesRepositoryImpl::class.java.simpleName)
    }

    override fun getPictures(): Flow<FetchResult> =
        flow {
            logger.info("Fetching pictures from API")
            val apiResult = api.getPictures()
            emit(apiResult.toResult())
        }.onStart {
            emit(FetchResult.Progress)
        }.catch {
            emit(FetchResult.Error)
        }.flowOn(defaultDispatcher)

    private fun List<RemotePicture>.toResult(): FetchResult =
        FetchResult.Success(pictures = map { picture -> picture.toDomain() })

    private fun RemotePicture.toDomain(): Picture =
        Picture(
            id = id,
            albumId = albumId,
            title = title,
            url = url,
            thumbnailUrl = thumbnailUrl
        )
}