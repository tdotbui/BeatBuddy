package edu.temple.beatbuddy.discover.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.discover.repository.FollowRepository
import edu.temple.beatbuddy.discover.repository.UsersRepository
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.user_auth.model.UserStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        fetchCurrentUserStats()
    }
    fun followCurrent() {
        _isFollowing.value = true
        _currentUser.value.let { user ->
            viewModelScope.launch {
                repository.follow(userId = user.id)
                fetchCurrentUserStats()
            }
        }
    }
    fun unfollowCurrent() {
        _isFollowing.value = false
        _currentUser.value.let { user ->
            viewModelScope.launch {
                repository.unfollow(userId = user.id)
                fetchCurrentUserStats()
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

    private fun fetchCurrentUserStats() {
        viewModelScope.launch {
            _currentUser.update {
                it.copy(stats = repository.fetchUserStats(userId = _currentUser.value.id).data)
            }
        }
    }
}