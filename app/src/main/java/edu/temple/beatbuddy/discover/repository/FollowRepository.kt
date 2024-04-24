package edu.temple.beatbuddy.discover.repository

import edu.temple.beatbuddy.utils.Resource

interface FollowRepository {
    suspend fun follow(userId: String): Resource<Boolean>
    suspend fun unfollow(userId: String): Resource<Boolean>
    suspend fun checkIfUserIsFollowed(userId: String): Resource<Boolean>
}