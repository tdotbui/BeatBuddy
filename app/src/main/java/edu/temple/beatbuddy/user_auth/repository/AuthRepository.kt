package edu.temple.beatbuddy.user_auth.repository

import com.google.firebase.auth.FirebaseUser
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.Resource

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun firebaseSignUpWithEmailAndPassword(
        email: String,
        password: String,
        fullName: String,
        username: String
    ): Resource<Boolean>

    suspend fun firebaseSignInWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<Boolean>

    fun signOut(): Resource<Boolean>

    suspend fun fetchCurrentUser(): Resource<User>
}