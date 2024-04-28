package edu.temple.beatbuddy.user_auth.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.user_auth.repository.AuthRepository
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    var userState = MutableStateFlow(UserState())
        private set

    fun signInWithEmailAndPassword(email: String, password: String) =
        viewModelScope.launch {
            userState.update {
                it.copy(isLoading = true)
            }
            repository.firebaseSignInWithEmailAndPassword(email, password).let { result->
                when(result) {
                    is Resource.Success -> {
                        userState.update {
                            it.copy(
                                isLoggedIn = true,
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        userState.update {
                            it.copy(
                                errorMessage = result.message,
                                isLoading = false
                            )
                        }
                    }

                    else -> {
                        userState.update {
                            it.copy(isLoading = true)
                        }
                    }
                }
            }
        }
}