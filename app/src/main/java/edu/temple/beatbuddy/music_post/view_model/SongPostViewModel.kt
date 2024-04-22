package edu.temple.beatbuddy.music_post.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.music_browse.model.local.Song
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.repository.SongPostRepository
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

    var currentSongPost = MutableStateFlow<SongPost?>(null)
        private set

    init {
        fetchSongPosts()
    }

    fun setCurrentSong(songPost: SongPost) {
        currentSongPost.value = songPost
    }

    private fun fetchSongPosts() = viewModelScope.launch {
        songPostState.update {
            it.copy(isLoading = true)
        }
        repository.fetchPostsFromFirestore().collectLatest { result ->
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
                        checkIfUserLikedPost()
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
                is Resource.Loading -> {
                    songPostState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    fun makePost(songPost: SongPost) = viewModelScope.launch {
        repository.shareAPost(songPost)
        fetchSongPosts()
    }

    private fun checkIfUserLikedPost() {
        for (post in songPostState.value.posts) {
            viewModelScope.launch {
                post.didLike = repository.checkIfUserLikePost(post).data
                Log.d("Post ${post.title}", "Did like is ${post.didLike}")
            }
        }
    }
}