package edu.temple.beatbuddy.repository

import edu.temple.beatbuddy.user_auth.model.MockUser
import edu.temple.beatbuddy.user_auth.model.UserStats
import edu.temple.beatbuddy.user_discover.repository.FollowRepository
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestFollowRepository: FollowRepository {
    override suspend fun follow(userId: String): Resource<Boolean> {
        return Resource.Success(true)
    }

    override suspend fun unfollow(userId: String): Resource<Boolean> {
        return Resource.Success(true)
    }

    override suspend fun checkIfUserIsFollowed(userId: String): Resource<Boolean> {
        return Resource.Success(true)
    }

    override suspend fun fetchUserStats(
        userId: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<UserStats>> = flow {
        val user = MockUser.users.find { it.id == userId }
        emit(Resource.Success(user?.stats))
    }
}