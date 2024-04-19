package edu.temple.beatbuddy.discover.view_model

import edu.temple.beatbuddy.user_auth.model.User

data class AllUsersState (
    val isLoading: Boolean = false,

    val users: List<User> = emptyList(),

    val errorMessage: String? = null
)