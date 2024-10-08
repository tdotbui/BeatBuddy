package edu.temple.beatbuddy.music_playlist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    var imageUrl: String = ""
)
