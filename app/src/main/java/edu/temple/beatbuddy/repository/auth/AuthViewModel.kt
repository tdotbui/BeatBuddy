package edu.temple.beatbuddy.repository.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val userRepository: UserRepository = UserRepository(
        FirebaseAuth.getInstance(),
        AuthInjection.instance()
    )

    private val _authResult = MutableLiveData<AuthResult<Boolean>>()
    val authResult: LiveData<AuthResult<Boolean>> = _authResult

    fun signUp(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = userRepository.signUp(fullName, email, password)
        }
    }
}