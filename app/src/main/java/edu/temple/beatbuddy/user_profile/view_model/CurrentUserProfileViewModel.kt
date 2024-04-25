package edu.temple.beatbuddy.user_profile.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.discover.repository.FollowRepository
import edu.temple.beatbuddy.user_auth.repository.AuthRepository
import edu.temple.beatbuddy.user_auth.view_model.UserState
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentUserProfileViewModel @Inject constructor(
    private val auth: AuthRepository,
//    private val repository: FollowRepository
): ViewModel() {
    var userState = MutableStateFlow(UserState())
        private set

    init {
        fetchCurrentUser()
//        fetchCurrentUserStats()
    }

//    private fun fetchCurrentUserStats() {
//        viewModelScope.launch {
//            userState.update { currentState ->
//                val currentUser = currentState.user
//                if (currentUser != null) {
//                    when (val statsResult = repository.fetchUserStats(currentUser.id)) {
//                        is Resource.Success -> {
//                            val updatedUser = currentUser.copy(stats = statsResult.data)
//                            currentState.copy(user = updatedUser)
//                        }
//                        is Resource.Error -> {
//                            currentState
//                        }
//                        else -> {
//                            currentState
//                        }
//                    }
//                } else {
//                    currentState
//                }
//            }
//        }
//    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            userState.update {
                it.copy(isLoading = true)
            }
            auth.fetchCurrentUser().let { result->
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

    fun signOut() = auth.signOut()
}