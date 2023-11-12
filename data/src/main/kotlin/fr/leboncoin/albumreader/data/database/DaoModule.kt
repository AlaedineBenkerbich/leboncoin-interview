package fr.leboncoin.albumreader.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    @Singleton
    fun provideDao(@ApplicationContext appContext: Context): Dao {
        val database = Room.databaseBuilder(
            context = appContext,
            klass = Database::class.java,
            name = "leboncoindb"
        ).build()

        return database.dao()
    }
}