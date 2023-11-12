package fr.leboncoin.albumreader.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pictures")
data class PictureEntity(
    @PrimaryKey var id: Long,
    @ColumnInfo var albumId: Long,
    @ColumnInfo var title: String,
    @ColumnInfo var url: String,
    @ColumnInfo var thumbnailUrl: String
)
