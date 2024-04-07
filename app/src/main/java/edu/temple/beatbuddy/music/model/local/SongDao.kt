package edu.temple.beatbuddy.music.model.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface SongDao {
    @Upsert
    suspend fun upsertSongList(songList: List<SongEntity>)

    @Query("SELECT * FROM SongEntity WHERE id = :id")
    suspend fun getSongById(id: Int): SongEntity

    @Query("SELECT * FROM SongEntity WHERE genre = :genre")
    suspend fun getSongListByGenre(genre: String): List<SongEntity>
}