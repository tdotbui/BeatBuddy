package edu.temple.beatbuddy.user_auth.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.temple.beatbuddy.user_auth.model.AuthRepository
import edu.temple.beatbuddy.user_auth.model.AuthResult.Success
import edu.temple.beatbuddy.user_auth.model.AuthResult.Error
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

    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.id).set(user).await()
    }
}