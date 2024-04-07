package edu.temple.beatbuddy.music.model.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streamable")
data class StreamableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val fulltrack: String
)
