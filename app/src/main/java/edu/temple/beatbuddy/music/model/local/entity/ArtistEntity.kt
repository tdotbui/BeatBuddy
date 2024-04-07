package edu.temple.beatbuddy.music.model.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist")
data class ArtistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val mbid: String,
    val url: String
)
