package edu.temple.beatbuddy.user_auth.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.user_auth.model.AuthRepository
import edu.temple.beatbuddy.user_auth.model.AuthResult.Initial
import edu.temple.beatbuddy.user_auth.model.AuthResult.Loading
import edu.temple.beatbuddy.user_auth.model.SignUpResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {

    var signUpResponse by mutableStateOf<SignUpResponse>(Initial)
        private set

    fun signUp(email: String, password: String, fullName: String) =
        viewModelScope.launch {
            signUpResponse = Loading
            signUpResponse = repo.firebaseSignUpWithEmailAndPassword(email, password, fullName)
        }

    fun resetResponse() {
        signUpResponse = Initial
    }
}