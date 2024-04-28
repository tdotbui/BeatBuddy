package edu.temple.beatbuddy.user_discover.view_model

import edu.temple.beatbuddy.user_auth.model.User

data class AllUsersState (
    val isLoading: Boolean = false,

    var users: List<User> = emptyList(),

    val errorMessage: String? = null
)