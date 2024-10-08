package edu.temple.beatbuddy.user_profile.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.user_discover.repository.FollowRepository
import edu.temple.beatbuddy.user_discover.repository.UsersRepository
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.user_auth.repository.AuthRepository
import edu.temple.beatbuddy.user_auth.view_model.UserState
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentUserProfileViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val repository: FollowRepository,
    private val userRepo: UsersRepository,
): ViewModel() {
    var userState = MutableStateFlow(UserState())
        private set

    private val _currentUser = MutableStateFlow(User())
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    var isViewingPosts = MutableStateFlow(true)
        private set

    init {
        fetchCurrentUser()
    }

    fun viewingPosts(posts: String) {
        isViewingPosts.value = posts == "Posts"
    }

    fun updateUserProfile(
        imageUrl: String,
        username: String,
        bio: String,
        shouldUpdate: Boolean
    ) {
        viewModelScope.launch {
            userRepo.updateProfile(imageUrl, username, bio, shouldUpdate).let {result ->
                if (result is Resource.Success) fetchCurrentUser()
            }
        }
    }

    fun fetchCurrentUserStats(fetchFromRemote: Boolean) {
        viewModelScope.launch {
            repository.fetchUserStats(
                userId = _currentUser.value.id,
                fetchFromRemote = fetchFromRemote
            ).collectLatest {result ->
                if (result is Resource.Success) {
                    result.data?.let {stats ->
                        _currentUser.update {
                            it.copy(stats = stats)
                        }
                    }
                }
            }
        }
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            userState.update {
                it.copy(isLoading = true)
            }
            auth.fetchCurrentUser().let { result->
                when(result) {
                    is Resource.Success -> {
                        result.data?.let { user ->
                            _currentUser.value = user
                            userState.update {
                                it.copy(
                                    user = user,
                                    isLoggedIn = true,
                                    isSignedUp = true,
                                    isLoading = false
                                )
                            }
                            fetchCurrentUserStats(true)
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