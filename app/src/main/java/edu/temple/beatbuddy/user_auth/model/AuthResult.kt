package edu.temple.beatbuddy.user_auth.model

sealed class AuthResult<out T> {

    data object Initial: AuthResult<Nothing>()
    data object Loading: AuthResult<Nothing>()
    data class Success<out T>(val data: T) : AuthResult<T>()
    data class Error(val exception: Exception) : AuthResult<Nothing>()
}