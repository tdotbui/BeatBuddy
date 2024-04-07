package edu.temple.beatbuddy.music.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.temple.beatbuddy.music.model.local.entity.SongEntity

@Database(
    entities = [SongEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SongDatabase: RoomDatabase() {
    abstract val songDao: SongDao
}