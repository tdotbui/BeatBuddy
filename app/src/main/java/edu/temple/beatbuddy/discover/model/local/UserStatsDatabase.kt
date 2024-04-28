package edu.temple.beatbuddy.discover.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.temple.beatbuddy.user_auth.model.UserStats

@Database(
    entities = [UserStats::class],
    version = 1,
    exportSchema = false
)
abstract class UserStatsDatabase: RoomDatabase() {
    abstract val userStatsDao: UserStatsDao
}