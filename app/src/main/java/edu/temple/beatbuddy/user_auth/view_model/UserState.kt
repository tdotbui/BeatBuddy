package edu.temple.beatbuddy.user_auth.view_model

import edu.temple.beatbuddy.user_auth.model.User
data class UserState (
    val isLoading: Boolean = false,

    val isLoggedIn: Boolean = false,
    val isSignedUp: Boolean = false,

    val user: User? = null,

    var errorMessage: String? = null
)