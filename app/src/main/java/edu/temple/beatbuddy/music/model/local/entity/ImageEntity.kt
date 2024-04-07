package edu.temple.beatbuddy.music.model.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val size: String
)

@Entity(tableName = "imageList")
data class ImageListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imageIdList: List<Long>
)

data class ImageList