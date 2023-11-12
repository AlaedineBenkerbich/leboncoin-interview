package fr.leboncoin.albumreader.data.repository

import fr.leboncoin.albumreader.common.core.DefaultDispatcher
import fr.leboncoin.albumreader.data.api.Api
import fr.leboncoin.albumreader.data.api.RemotePicture
import fr.leboncoin.albumreader.data.database.Dao
import fr.leboncoin.albumreader.data.database.PictureEntity
import fr.leboncoin.albumreader.domain.model.PictureData
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
    private val dao: Dao,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : PicturesRepository {
    companion object {
        private val logger = Logger.getLogger(PicturesRepositoryImpl::class.java.simpleName)
    }

    override fun getPictures(): Flow<FetchResult> =
        flow {
            val localResult = dao.getPictures()
            when (localResult.isEmpty()) {
                true -> {
                    logger.info("Fetching pictures from API")
                    val apiResult = api.getPictures()
                    emit(apiResult.remoteToResult())
                    dao.insert(pictures = apiResult.toEntities())
                }

                false -> {
                    logger.info("Fetching pictures from local database")
                    emit(localResult.localToResult())
                }
            }
        }.onStart {
            emit(FetchResult.Progress)
        }.catch {
            val exception = it as? Exception
            logger.info("Error while fetching pictures: $exception")
            emit(FetchResult.Error)
        }.flowOn(defaultDispatcher)

    private fun List<RemotePicture>.remoteToResult(): FetchResult =
        FetchResult.Success(pictures = map { picture -> picture.remoteToDomain() })

    private fun RemotePicture.remoteToDomain(): PictureData =
        PictureData(
            id = id,
            albumId = albumId,
            title = title,
            url = url,
            thumbnailUrl = thumbnailUrl
        )

    private fun List<PictureEntity>.localToResult(): FetchResult =
        FetchResult.Success(pictures = map { picture -> picture.localToDomain() })

    private fun PictureEntity.localToDomain(): PictureData =
        PictureData(
            id = id.toInt(),
            albumId = albumId.toInt(),
            title = title,
            url = url,
            thumbnailUrl = thumbnailUrl
        )

    private fun List<RemotePicture>.toEntities(): List<PictureEntity> =
        map { remotePicture -> remotePicture.toEntity() }

    private fun RemotePicture.toEntity(): PictureEntity = PictureEntity(
        id = id.toLong(),
        albumId = albumId.toLong(),
        title = title,
        url = url,
        thumbnailUrl = thumbnailUrl
    )
}