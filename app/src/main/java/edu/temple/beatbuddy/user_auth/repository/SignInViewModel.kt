package edu.temple.beatbuddy.user_auth.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.user_auth.model.AuthRepository
import edu.temple.beatbuddy.user_auth.model.AuthResult.Loading
import edu.temple.beatbuddy.user_auth.model.AuthResult.Initial
import edu.temple.beatbuddy.user_auth.model.SignInResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    var signInResponse by mutableStateOf<SignInResponse>(Initial)
        private set

    fun signInWithEmailAndPassword(email: String, password: String) =
        viewModelScope.launch {
            signInResponse = Loading
            signInResponse = repo.firebaseSignInWithEmailAndPassword(email, password)
        }

    fun resetResponse() {
        signInResponse = Initial
    }
}