package edu.temple.beatbuddy.user_auth.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRef: CollectionReference
) : AuthRepository {

    override val currentUser get() = auth.currentUser
    override suspend fun firebaseSignUpWithEmailAndPassword(
        email: String,
        password: String,
        fullName: String,
        username: String
    ): Resource<Boolean> = try {
        auth.createUserWithEmailAndPassword(email, password).await()
        currentUser?.let {
            val user = User(id = it.uid, fullName = fullName, email = email, username = username)
            saveUserToFirestore(user = user)
        }
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!, false)
    }

    override suspend fun firebaseSignInWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<Boolean> = try {
        auth.signInWithEmailAndPassword(email, password).await()
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!, false)
    }

    override fun signOut(): Resource<Boolean> = try {
        if (currentUser != null) {
            auth.signOut()
        }
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!, false)
    }
    override suspend fun fetchCurrentUser(): Resource<User> = try {
        val userId = currentUser?.uid
        val user = userId?.let {
            userRef.document(it).get().await().toObject(User::class.java)
        }
        Resource.Success(user)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!, User())
    }

    private suspend fun saveUserToFirestore(user: User) {
        userRef.document(user.id).set(user).await()
    }
}