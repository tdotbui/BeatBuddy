package edu.temple.beatbuddy.user_auth.model

import com.google.firebase.auth.FirebaseUser

typealias SignUpResponse = AuthResult<Boolean>
typealias SignInResponse = AuthResult<Boolean>
typealias FetchCurrentUserResponse = AuthResult<User>

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun firebaseSignUpWithEmailAndPassword(
        email: String,
        password: String,
        fullName: String
    ): SignUpResponse

    suspend fun firebaseSignInWithEmailAndPassword(
        email: String,
        password: String
    ): SignInResponse

    fun signOut()

    suspend fun fetchCurrentUser(): FetchCurrentUserResponse
}