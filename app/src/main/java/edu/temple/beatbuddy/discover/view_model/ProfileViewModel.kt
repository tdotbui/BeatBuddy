package edu.temple.beatbuddy.discover.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.discover.repository.FollowRepository
import edu.temple.beatbuddy.discover.repository.UsersRepository
import edu.temple.beatbuddy.music_player.player.PlayerState
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.user_auth.model.UserStats
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: FollowRepository
): ViewModel() {

    private val _currentUser = MutableStateFlow(User())
    val currentUser: StateFlow<User> = _currentUser

    private val _isFollowing = MutableStateFlow(false)
    val isFollowing = _isFollowing

    fun setCurrentUser(user: User) {
        _currentUser.value = user
        checkIfCurrentUserIsFollowed()
        fetchCurrentUserStats(false)
    }
    fun followCurrent() {
        _isFollowing.value = true
        _currentUser.value.let { user ->
            viewModelScope.launch {
                repository.follow(userId = user.id)
                fetchCurrentUserStats(true)
            }
        }
    }
    fun unfollowCurrent() {
        _isFollowing.value = false
        _currentUser.value.let { user ->
            viewModelScope.launch {
                repository.unfollow(userId = user.id)
                fetchCurrentUserStats(true)
            }
        }
    }

    private fun checkIfCurrentUserIsFollowed() {
        viewModelScope.launch {
            _isFollowing.value = repository.checkIfUserIsFollowed(userId = _currentUser.value.id).data == true
            _currentUser.update {
                it.copy(isFollowed = _isFollowing.value)
            }
        }
    }

    private fun fetchCurrentUserStats(fetchFromRemote: Boolean) {
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
}