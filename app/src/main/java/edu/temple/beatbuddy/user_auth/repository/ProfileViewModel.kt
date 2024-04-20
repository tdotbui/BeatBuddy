package edu.temple.beatbuddy.user_auth.repository

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.user_auth.model.AuthRepository
import edu.temple.beatbuddy.user_auth.model.AuthResult
import edu.temple.beatbuddy.user_auth.model.FetchCurrentUserResponse
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.Helpers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    var currentUserResponse by mutableStateOf<FetchCurrentUserResponse>(AuthResult.Initial)
        private set

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            currentUserResponse = AuthResult.Loading
            currentUserResponse = try {
                repo.fetchCurrentUser()
            } catch (e: Exception) {
                AuthResult.Error(e)
            }
        }
    }

    fun signOut() = repo.signOut()
}