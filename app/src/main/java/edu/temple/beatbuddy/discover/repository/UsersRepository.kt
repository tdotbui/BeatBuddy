package edu.temple.beatbuddy.discover.repository

import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun fetchAllUsersFromFireStore(): Flow<Resource<List<User>>>

    suspend fun fetchUser(uid: String): Resource<User>
}