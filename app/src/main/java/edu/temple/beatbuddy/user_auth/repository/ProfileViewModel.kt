package edu.temple.beatbuddy.user_auth.repository

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.user_auth.model.AuthRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {

    fun signOut() = repo.signOut()
}