package edu.temple.beatbuddy.music.model.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.temple.beatbuddy.music.model.local.entity.SongEntity

@Dao
interface SongDao {
    @Upsert
    suspend fun upsertSongList(songList: List<SongEntity>)

    @Query("SELECT * FROM SongEntity WHERE id = :id")
    suspend fun getSongById(id: Long): SongEntity
}