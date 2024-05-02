package edu.temple.beatbuddy.user_discover.repository

import edu.temple.beatbuddy.user_auth.model.UserStats
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FollowRepository {
    suspend fun follow(userId: String): Resource<Boolean>
    suspend fun unfollow(userId: String): Resource<Boolean>
    suspend fun checkIfUserIsFollowed(userId: String): Resource<Boolean>

    suspend fun fetchUserStats(userId: String, fetchFromRemote: Boolean): Flow<Resource<UserStats>>
}