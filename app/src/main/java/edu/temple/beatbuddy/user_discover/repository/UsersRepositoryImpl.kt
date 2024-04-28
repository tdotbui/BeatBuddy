package edu.temple.beatbuddy.user_discover.repository

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val usersRef: CollectionReference,
): UsersRepository {
    override fun fetchAllUsersFromFireStore() = callbackFlow {
        val userUid = auth.currentUser?.uid
        val snapshotListener = usersRef.addSnapshotListener { snapshot, e ->
            val response = if (snapshot != null) {
                val users = snapshot.toObjects(User::class.java).filter {
                    it.id != userUid
                }
                Resource.Success(users)
            } else {
                Resource.Error(e?.message!!)
            }
            trySend(response)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun updateProfile(
        imageUrl: String,
        username: String,
        bio: String,
        shouldUpdate: Boolean
    ): Resource<Boolean> = try {
        val userUid = auth.currentUser?.uid
        userUid?.let {
            if (shouldUpdate) {
                val image = uploadProfileImage(imageUrl.toUri())
                val updateData = hashMapOf<String, Any>(
                    "profileImage" to image,
                    "username" to username,
                    "bio" to bio
                )
                usersRef.document(userUid).update(updateData).await()
            } else {
                val updateData = hashMapOf<String, Any>(
                    "username" to username,
                    "bio" to bio
                )
                usersRef.document(userUid).update(updateData).await()
            }
        }

        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!, false)
    }

    private suspend fun uploadProfileImage(imageUri: Uri): String = try {
        val userUid = auth.currentUser?.uid
        val storageRef = userUid?.let {
            FirebaseStorage.getInstance().reference
                .child("profile_images")
                .child(it)
        }
        storageRef?.putFile(imageUri)?.await()
        storageRef?.downloadUrl?.await().toString()
    } catch (e: Exception) { "" }
}