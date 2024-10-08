package edu.temple.beatbuddy.user_discover.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import edu.temple.beatbuddy.user_discover.model.local.UserStatsDatabase
import edu.temple.beatbuddy.user_auth.model.UserStats
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FollowRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val followersRef: CollectionReference,
    private val followingRef: CollectionReference,
    private val postRef: CollectionReference,
    private val userStatsDb: UserStatsDatabase
): FollowRepository {
    override suspend fun follow(userId: String): Resource<Boolean> = try {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId != null) {
            runCatching {
                followingRef
                    .document(currentUserId)
                    .collection("user-following")
                    .document(userId)
                    .set(mapOf<Any, String>())
                    .await()
            }

            runCatching {
                followersRef
                    .document(userId)
                    .collection("user-followers")
                    .document(currentUserId)
                    .set(mapOf<Any, String>())
                    .await()
            }
        }
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!, false)
    }

    override suspend fun unfollow(userId: String): Resource<Boolean> = try {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId != null) {
            runCatching {
                followingRef
                    .document(currentUserId)
                    .collection("user-following")
                    .document(userId)
                    .delete()
            }

            runCatching {
                followersRef
                    .document(userId)
                    .collection("user-followers")
                    .document(currentUserId)
                    .delete()
            }
        }
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!, false)
    }

    override suspend fun checkIfUserIsFollowed(userId: String): Resource<Boolean> = try {
        val currentUserId = auth.currentUser?.uid ?: ""

        val snapshot = followingRef
            .document(currentUserId)
            .collection("user-following")
            .document(userId)
            .get()
            .await()
        Resource.Success(snapshot.exists())
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!, false)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun fetchUserStats(userId: String, fetchFromRemote: Boolean): Flow<Resource<UserStats>> = flow {
        emit(Resource.Loading(true))
        val localUserStats = userStatsDb.userStatsDao.getUserStatsById(userId)

        if (!fetchFromRemote && localUserStats != null) {
            emit(Resource.Success(localUserStats))
            emit(Resource.Loading(false))
            return@flow
        }

        val followingCountDeferred = GlobalScope.async {
            runCatching {
                followingRef.document(userId)
                    .collection("user-following")
                    .get()
                    .await()
                    .size()
            }.getOrElse { 0 }
        }
        val followerCountDeferred = GlobalScope.async {
            runCatching {
                followersRef.document(userId)
                    .collection("user-followers")
                    .get()
                    .await()
                    .size()
            }.getOrElse { 0 }
        }
        val postCountDeferred = GlobalScope.async {
            runCatching {
                postRef.whereEqualTo("ownerUid", userId)
                    .get()
                    .await()
                    .size()
            }.getOrElse { 0 }
        }

        val followingCount = followingCountDeferred.await()
        val followerCount = followerCountDeferred.await()
        val postCount = postCountDeferred.await()
        val userStats = UserStats(userId, followingCount, followerCount, postCount)

        userStatsDb.userStatsDao.upsertUser(listOf(userStats))
        emit(Resource.Success(userStats))
        emit(Resource.Loading(false))
    }
}