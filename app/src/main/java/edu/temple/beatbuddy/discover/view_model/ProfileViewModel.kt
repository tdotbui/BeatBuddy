package edu.temple.beatbuddy.discover.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.discover.repository.UsersRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UsersRepository
): ViewModel() {

    fun follow() {
        Log.d("DEBUG", "Follow the user")
    }

    fun unfollow() {
        Log.d("DEBUG", "Unfollow the user")
    }
}