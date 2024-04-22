package edu.temple.beatbuddy.music_post.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
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
import javax.inject.Inject
import kotlin.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SongPostRepositoryImpl @Inject constructor(
    private val postRef: CollectionReference,
    private val userRef: CollectionReference,
): SongPostRepository {

    override fun fetchPostsFromFirestore(): Flow<Resource<List<SongPost>>> = flow {
        emit(Resource.Loading(true))
        try {
            val posts = postRef.get().await().toObjects(SongPost::class.java).map {
                val user = userRef.document(it.ownerUid).get().await().toObject(User::class.java)
                it.copy(user = user)
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
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!)
    }

    override suspend fun deleteAPost(postId: String): Resource<Boolean> = try {
        postRef.document(postId).delete().await()
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!)
    }

    override suspend fun likePost(songPost: SongPost): Resource<Boolean> = try {
        val userUid = FirebaseAuth.getInstance().currentUser?.uid
        val postId = songPost.postId

        if (userUid != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val likePostDeferred = async {
                    postRef
                        .document(postId)
                        .collection("post-likes")
                        .document(userUid)
                        .set(mapOf<String, Any>())
                }

                val updateLikesDeferred = async {
                    postRef
                        .document(postId)
                        .update("likes", songPost.likes)
                }

                val userLikeDeferred = async {
                    userRef
                        .document(userUid)
                        .collection("user-likes")
                        .document(postId)
                        .set(mapOf<String, Any>())
                }

                likePostDeferred.await()
                updateLikesDeferred.await()
                userLikeDeferred.await()
            }
        }

        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!)
    }

    override suspend fun unlikePost(songPost: SongPost): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun checkIfUserLikePost(songPost: SongPost): Resource<Boolean> = try {
        val userUid = FirebaseAuth.getInstance().currentUser?.uid
        val postId = songPost.postId
        val snapshot = userUid?.let { userRef.document(it).collection("user-likes").document(postId).get().await() }
        Resource.Success(snapshot?.exists())
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!)
    }
}


