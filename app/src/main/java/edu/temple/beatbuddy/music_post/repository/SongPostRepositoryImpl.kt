package edu.temple.beatbuddy.music_post.repository

import com.google.firebase.firestore.CollectionReference
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

//    private val coroutineScope = CoroutineScope(Dispatchers.IO)
//    override fun fetchPostsFromFirestore(): Flow<Resource<List<SongPost>>> = callbackFlow {
//        val snapshotListener = postRef.addSnapshotListener { snapshot, e ->
//            val response = if (snapshot != null) {
//                val posts = snapshot.toObjects(SongPost::class.java).map { songPost ->
//                    coroutineScope.async {
//                        val user = getUser(songPost.ownerUid)
//                        songPost.copy(user = user)
//                    }
//                }
//                val postList = coroutineScope.async { posts.awaitAll() }
//                Resource.Success(postList)
//            } else {
//                Resource.Error(e?.message ?: "Snapshot is null")
//            }
//            trySend(response)
//        }
//        awaitClose {
//            snapshotListener.remove()
//        }
//    }

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

    private suspend fun getUser(ownerUid: String): User? {
        return withContext(Dispatchers.IO) {
            val userDocument = userRef.document(ownerUid).get().await()
            userDocument.toObject(User::class.java)
        }
    }
}


