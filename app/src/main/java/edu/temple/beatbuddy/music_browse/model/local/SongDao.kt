package edu.temple.beatbuddy.music_browse.model.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface SongDao {
    @Upsert
    suspend fun upsertSongList(songList: List<Song>)

    @Query("SELECT * FROM SongEntity WHERE id = :id")
    suspend fun getSongById(id: Long): Song?

    @Query("SELECT * FROM SongEntity WHERE genre = :genre")
    suspend fun getSongListByGenre(genre: Int): List<Song>
}