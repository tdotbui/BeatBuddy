package edu.temple.beatbuddy.music_post.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import edu.temple.beatbuddy.discover.repository.UsersRepository
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SongPostRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val postRef: CollectionReference,
    private val userRef: CollectionReference,
    private val followersRef: CollectionReference
): SongPostRepository {

    override fun fetchAllPostsFromFirestore(): Flow<Resource<List<SongPost>>> = flow {
        emit(Resource.Loading(true))
        try {
            val posts = postRef.orderBy("timestamp", Query.Direction.DESCENDING).get().await().toObjects(SongPost::class.java).map {
                val user = userRef.document(it.ownerUid).get().await().toObject(User::class.java)
                it.copy(user = user, didLike = checkIfUserLikedPost(it).data)
            }
            emit(Resource.Success(posts))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
        emit(Resource.Loading(false))
    }

    override suspend fun shareAPost(songPost: SongPost): Resource<Boolean> = try {
        val id = postRef.document().id
        val post = songPost.copy(postId = id)
        postRef.document(id).set(post).await()

        updateUserFeedAfterPost(id)

        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!, false)
    }

    override fun fetchPostsFromFollowing(): Flow<Resource<List<SongPost>>> = flow {
        emit(Resource.Loading(true))
        try {
            val currentUid = auth.currentUser?.uid ?: ""
            val postSnapshot = userRef
                .document(currentUid)
                .collection("user-feed")
                .get()
                .await()
            val posts: MutableList<SongPost> = mutableListOf()

            for (doc in postSnapshot.documents) {
                val post = postRef.document(doc.id).get().await().toObject(SongPost::class.java)?.let {
                    val user = userRef.document(it.ownerUid).get().await().toObject(User::class.java)
                    it.copy(user = user)
                }
                if (post != null) {
                    posts.add(post)
                }
            }

            emit(Resource.Success(posts))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
        emit(Resource.Loading(false))
    }

    override suspend fun likePost(songPost: SongPost): Resource<Boolean> = try {
        auth.currentUser?.uid?.let {uid ->
            runCatching {
                postRef
                    .document(songPost.postId)
                    .collection("post-likes")
                    .document(uid)
                    .set(mapOf<String, Any>())
                    .await()
            }
            runCatching {
                postRef
                    .document(songPost.postId)
                    .update("likes", songPost.likes + 1)
                    .await()
            }
            runCatching {
                userRef
                    .document(uid)
                    .collection("user-likes")
                    .document(songPost.postId)
                    .set(mapOf<String, Any>())
                    .await()
            }
        }
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!, false)
    }

    override suspend fun unlikePost(songPost: SongPost): Resource<Boolean> = try {
        auth.currentUser?.uid?.let {uid ->
            runCatching {
                postRef
                    .document(songPost.postId)
                    .collection("post-likes")
                    .document(uid)
                    .delete()
            }
            runCatching {
                postRef
                    .document(songPost.postId)
                    .update("likes", songPost.likes - 1)
                    .await()
            }
            runCatching {
                userRef
                    .document(uid)
                    .collection("user-likes")
                    .document(songPost.postId)
                    .delete()
            }
        }
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!, false)
    }

    override suspend fun checkIfUserLikedPost(songPost: SongPost): Resource<Boolean> = try {
        val userId = auth.currentUser?.uid ?: ""
        val snapshot = userRef
            .document(userId)
            .collection("user-likes")
            .document(songPost.postId)
            .get()
            .await()
        Resource.Success(snapshot.exists())
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!, false)
    }

    private suspend fun updateUserFeedAfterPost(postId: String) {
        val currentUid = auth.currentUser?.uid ?: ""

        val followersSnapshot = followersRef
            .document(currentUid)
            .collection("user-followers")
            .get()
            .await()

        for (doc in followersSnapshot.documents) {
            userRef
                .document(doc.id)
                .collection("user-feed")
                .document(postId)
                .set(mapOf<String, Any>())
                .await()
        }
    }
}


