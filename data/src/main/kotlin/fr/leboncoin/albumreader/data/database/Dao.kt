package fr.leboncoin.albumreader.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {

    @Query("SELECT * FROM pictures")
    suspend fun getPictures(): List<PictureEntity>

    @Insert
    suspend fun insert(pictures: List<PictureEntity>)
}