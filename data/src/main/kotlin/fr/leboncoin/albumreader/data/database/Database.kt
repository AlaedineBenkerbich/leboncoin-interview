package fr.leboncoin.albumreader.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PictureEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun dao(): Dao
}