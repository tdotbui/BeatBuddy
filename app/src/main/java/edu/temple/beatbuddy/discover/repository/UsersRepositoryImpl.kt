package edu.temple.beatbuddy.discover.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.toObject
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
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
}