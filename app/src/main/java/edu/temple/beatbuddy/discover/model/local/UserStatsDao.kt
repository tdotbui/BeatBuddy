package edu.temple.beatbuddy.discover.model.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.temple.beatbuddy.user_auth.model.UserStats

@Dao
interface UserStatsDao {
    @Upsert
    suspend fun upsertUser(userStatsList: List<UserStats>)

    @Query("SELECT * FROM UserStats WHERE id = :id")
    suspend fun getUserStatsById(id: String): UserStats
}