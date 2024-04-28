package edu.temple.beatbuddy.music_playlist.model.local

import androidx.room.Delete
import androidx.room.Insert
import edu.temple.beatbuddy.music_playlist.model.PlaylistSong

interface PlaylistSongDao {
    @Insert
    suspend fun insertSong(song: PlaylistSong)

    @Delete
    suspend fun deleteSong(song: PlaylistSong)
}