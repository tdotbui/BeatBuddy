package edu.temple.beatbuddy.discover.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.discover.repository.FollowRepository
import edu.temple.beatbuddy.discover.repository.UsersRepository
import edu.temple.beatbuddy.user_auth.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: FollowRepository
): ViewModel() {

    var isFollowing = MutableStateFlow(false)
        private set

    fun follow(user: User) = viewModelScope.launch {
        repository.follow(userId = user.id)
    }

    fun unfollow(user: User) = viewModelScope.launch {
        repository.unfollow(userId = user.id)
    }

    fun checkIfUserIsFollowed(user: User) = viewModelScope.launch {
        repository.checkIfUserIsFollowed(userId = user.id).let {
            isFollowing.value = it.data == true
            Log.d("Result", it.data.toString())
        }
    }

    fun handleFollow() {
        isFollowing.value = !isFollowing.value
    }
}