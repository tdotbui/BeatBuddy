package edu.temple.beatbuddy.music_browse.model.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Song::class],
    version = 1,
    exportSchema = false
)
abstract class SongDatabase: RoomDatabase() {
    abstract val songDao: SongDao
}