package edu.temple.beatbuddy.user_discover.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.user_discover.repository.UsersRepository
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllUsersViewModel @Inject constructor(
    private val repository: UsersRepository
): ViewModel() {
    var allUsersState = MutableStateFlow(AllUsersState())
        private set

    init {
        fetchAllUsers()
    }

    fun fetchAllUsers() = viewModelScope.launch {
        allUsersState.update {
            it.copy(isLoading = true)
        }

        repository.fetchAllUsersFromFireStore().collectLatest { result ->
            when(result) {
                is Resource.Success -> {
                    result.data?.let {users ->
                        allUsersState.update {
                            it.copy(
                                users = users,
                                isLoading = false
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    allUsersState.update {
                        it.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> {
                    allUsersState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
}