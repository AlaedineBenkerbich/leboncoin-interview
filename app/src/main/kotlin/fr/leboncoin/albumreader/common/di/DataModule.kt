package fr.leboncoin.albumreader.common.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fr.leboncoin.albumreader.data.PicturesRepositoryImpl
import fr.leboncoin.albumreader.domain.repository.PicturesRepository

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindPicturesRepository(binding: PicturesRepositoryImpl): PicturesRepository
}