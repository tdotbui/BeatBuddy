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
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    var userState = MutableStateFlow(UserState())
        private set

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            userState.update {
                it.copy(isLoading = true)
            }
            repository.fetchCurrentUser().let { result->
                when(result) {
                    is Resource.Success -> {
                        result.data?.let { user ->
                            userState.update {
                                it.copy(
                                    user = user,
                                    isLoggedIn = true,
                                    isSignedUp = true,
                                    isLoading = false
                                )
                            }
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

    fun signOut() = repository.signOut()
}