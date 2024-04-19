package edu.temple.beatbuddy.discover.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AllUsersRepositoryImpl @Inject constructor(
    private val usersRef: CollectionReference,
): AllUsersRepository {
    override fun fetchAllUsersFromFireStore() = callbackFlow {
        val snapshotListener = usersRef.addSnapshotListener { snapshot, e ->
            val response = if (snapshot != null) {
                val users = snapshot.toObjects(User::class.java)
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