package edu.temple.beatbuddy.music_playlist.model.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import edu.temple.beatbuddy.music_playlist.model.Playlist
import edu.temple.beatbuddy.music_playlist.model.PlaylistWithSongs

interface PlaylistDao {
    @Insert
    suspend fun insertPlaylist(playlist: Playlist)

    @Delete
    suspend fun deletePlaylist(playlist: Playlist)

    @Query("SELECT * FROM Playlist")
    fun getAllPlaylists(): List<Playlist>

    @Query("SELECT * FROM Playlist WHERE id = :playlistId")
    fun getPlaylistById(playlistId: Long): PlaylistWithSongs
}