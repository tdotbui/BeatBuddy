package edu.temple.beatbuddy.repository

import com.google.firebase.auth.FirebaseUser
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.user_auth.repository.AuthRepository
import edu.temple.beatbuddy.utils.Resource
import org.mockito.Mockito.mock

class TestAuthRepository(override val currentUser: FirebaseUser?) : AuthRepository {

    override suspend fun firebaseSignUpWithEmailAndPassword(
        email: String,
        password: String,
        fullName: String,
        username: String
    ): Resource<Boolean> {
        return Resource.Success(true)
    }

    override suspend fun firebaseSignInWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<Boolean> {
        return Resource.Success(true)
    }

    override fun signOut(): Resource<Boolean> {
        return Resource.Success(true)
    }

    override suspend fun fetchCurrentUser(): Resource<User> {
        val user = User("Test User", "test@example.com")
        return Resource.Success(user)
    }
}