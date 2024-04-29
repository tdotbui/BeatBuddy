package edu.temple.beatbuddy.music_playlist.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import edu.temple.beatbuddy.music_playlist.model.PlaylistSong
import edu.temple.beatbuddy.music_playlist.model.PlaylistSongCrossRef

@Dao
interface PlaylistSongDao {
    @Insert
    suspend fun insertSong(song: PlaylistSong): Long

    @Delete
    suspend fun deleteSong(song: PlaylistSong)

    @Query("SELECT * FROM PlaylistSong WHERE songId IN (SELECT songId FROM PlaylistSongCrossRef WHERE playlistId = :playlistId)")
    suspend fun getSongsForPlaylist(playlistId: Long): List<PlaylistSong>


//    @Query("SELECT * FROM PlaylistSong WHERE songId IN (:songIds)")
//    suspend fun getSongsForPlaylists(songIds: List<Long>): List<PlaylistSong>

    @Query("DELETE FROM PlaylistSongCrossRef WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long)
}