package edu.temple.beatbuddy.music_playlist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaylistSong(
    @PrimaryKey
    val songId: Long = 0L,
    val title: String = "",
    val preview: String = "",
    val artistName: String = "",
    val artistPicture: String = "",
    val songImage: String = "",
)
