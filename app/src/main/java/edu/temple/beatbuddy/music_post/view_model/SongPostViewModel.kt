package edu.temple.beatbuddy.music_post.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.repository.SongPostRepository
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongPostViewModel @Inject constructor(
    private val repository: SongPostRepository
): ViewModel() {

    var songPostState = MutableStateFlow(SongPostState())
        private set

    var userSongPostState = MutableStateFlow(SongPostState())
        private set
    var currentSongPost = MutableStateFlow<SongPost?>(null)
        private set

    init {
        fetchSongPosts()
    }

    fun fetchSongPosts() = viewModelScope.launch {
        songPostState.update {
            it.copy(isLoading = true)
        }
        repository.fetchAllPostsFromFirestore().collectLatest { result ->
            when(result) {
                is Resource.Success -> {
                    Log.d("Success", "Success from Firestore")
                    result.data?.let {posts ->
                        songPostState.update {
                            it.copy(
                                posts = posts,
                                isLoading = false
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    songPostState.update {
                        it.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun fetchPostForUser(user: User) = viewModelScope.launch {
        userSongPostState.update {
            it.copy(isLoading = true)
        }
        repository.fetchPostsForUser(user).collectLatest { result ->
            when(result) {
                is Resource.Success -> {
                    result.data?.let {posts ->
                        userSongPostState.update {
                            it.copy(
                                posts = posts,
                                isLoading = false
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    userSongPostState.update {
                        it.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                }
                else -> {}
            }
        }
    }


    fun likePost(songPost: SongPost) {
        viewModelScope.launch {
            repository.likePost(songPost).let {result ->
//                if (result is Resource.Success) fetchSongPosts()
            }
        }
    }

    fun unlikePost(songPost: SongPost) {
        viewModelScope.launch {
            repository.unlikePost(songPost).let {result ->
//                if (result is Resource.Success) fetchSongPosts()
            }
        }
    }

    fun makePost(songPost: SongPost) = viewModelScope.launch {
        repository.shareAPost(songPost).let { result ->
            if (result is Resource.Success) {
                fetchSongPosts()
            }
        }
    }

    fun deletePost(songPost: SongPost, user: User) = viewModelScope.launch {
        repository.deleteAPost(songPost).let { result ->
            if (result is Resource.Success) {
                fetchPostForUser(user)
                fetchSongPosts()
            }
        }

    }

    fun setCurrentSongPost(songPost: SongPost) {
        currentSongPost.value = songPost
    }

    fun clearCurrentSongPost() {
        currentSongPost.value = null
    }
}