package edu.temple.beatbuddy.repository

import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.user_discover.repository.UsersRepository
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestUserRepository: UsersRepository {

    private val userList = mutableListOf<User>()

    override fun fetchAllUsersFromFireStore(): Flow<Resource<List<User>>> = flow {
        emit(Resource.Success(userList))
    }

    override suspend fun updateProfile(
        imageUrl: String,
        username: String,
        bio: String,
        shouldUpdate: Boolean
    ): Resource<Boolean> {
        val user = userList.find { it.username == username }
        val tempUser = user?.copy(
            username = username,
            bio = bio,
            profileImage = imageUrl
        )
        if (tempUser != null) {
            userList.remove(user)
            userList.add(tempUser)
        }
        return Resource.Success(userList.last().bio == bio && userList.last().profileImage == imageUrl)
    }
}