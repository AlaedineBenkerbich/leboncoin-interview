package fr.leboncoin.albumreader.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.albumreader.common.core.DefaultDispatcher
import fr.leboncoin.albumreader.common.core.IoDispatcher
import fr.leboncoin.albumreader.common.core.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @see <a href="https://developer.android.com/kotlin/coroutines/coroutines-best-practices">
 *     https://developer.android.com/kotlin/coroutines/coroutines-best-practices</a>
 */
@Module
@InstallIn(SingletonComponent::class)
internal object CoroutineDispatcherModule {

    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}