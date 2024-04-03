package edu.temple.beatbuddy.user_auth.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import edu.temple.beatbuddy.user_auth.model.AuthRepository
import edu.temple.beatbuddy.user_auth.model.AuthResult.Success
import edu.temple.beatbuddy.user_auth.model.AuthResult.Error
import edu.temple.beatbuddy.user_auth.model.FetchCurrentUserResponse
import edu.temple.beatbuddy.user_auth.model.SignInResponse
import edu.temple.beatbuddy.user_auth.model.SignUpResponse
import edu.temple.beatbuddy.user_auth.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override val currentUser get() = auth.currentUser
    override suspend fun firebaseSignUpWithEmailAndPassword(
        email: String,
        password: String,
        fullName: String
    ): SignUpResponse = try {
        auth.createUserWithEmailAndPassword(email, password).await()
        currentUser?.let {
            val user = User(id = it.uid, fullName = fullName, email = email)
            saveUserToFirestore(user = user)
        }
        Success(true)
    } catch (e: Exception) {
        Error(e)
    }

    override suspend fun firebaseSignInWithEmailAndPassword(
        email: String,
        password: String
    ): SignInResponse = try {
        auth.signInWithEmailAndPassword(email, password).await()
        Success(true)
    } catch (e: Exception) {
        Error(e)
    }

    override fun signOut() = auth.signOut()
    override suspend fun fetchCurrentUser(): FetchCurrentUserResponse = try {
        val userId = currentUser?.uid
        val user = userId?.let {
            val documentSnapshot = firestore.collection("users").document(it).get().await()
            val data = documentSnapshot.data
            if (data != null) {
                val gson = Gson()
                gson.fromJson(gson.toJson(data), User::class.java)
            } else {
                User("", "", "")
            }
        } ?: User("", "", "")
        Success(user)
    } catch (e: Exception) {
        Error(e)
    }

    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.id).set(user).await()
    }
}