package edu.temple.beatbuddy.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.temple.beatbuddy.model.User
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun signUp(fullName: String, email: String, password: String) : AuthResult<Boolean> =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            auth.currentUser?.let {
                val user = User(id = it.uid, fullName = fullName, email = email)
                saveUserToFirestore(user = user)
            }
            AuthResult.Success(true)
        } catch (e: Exception) {
            AuthResult.Error(e)
        }
    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.id).set(user).await()
    }
}