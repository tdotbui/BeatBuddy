package edu.temple.beatbuddy.user_auth.model

import com.google.firebase.auth.FirebaseUser

typealias SignUpResponse = AuthResult<Boolean>
interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun firebaseSignUpWithEmailAndPassword(email: String, password: String, fullName: String): SignUpResponse
}