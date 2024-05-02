package edu.temple.beatbuddy.music_swipe.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.music_browse.model.mapping.toPlaylistSong
import edu.temple.beatbuddy.music_post.model.MockPost
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.repository.SongPostRepository
import edu.temple.beatbuddy.music_post.view_model.SongPostState
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwipeSongViewModel @Inject constructor(
    private val repository: SongPostRepository,
): ViewModel() {

    var songPostState = MutableStateFlow(SongPostState())
        private set

    var currentSongPost = MutableStateFlow<SongPost?>(null)
        private set

    init {
        fetchSwipeSongPosts()
    }

    fun setCurrentSong(songPost: SongPost) {
        currentSongPost.value = songPost
    }

    fun removeSongFromList() = viewModelScope.launch {
        val remainingPosts = songPostState.value.posts.toMutableList().apply {
            val post = removeFirst()
            if (post.didLike == false) {
                likePost(post)
            }
            repository.deletePostFromFollowing(post)
        }
        songPostState.update {
            it.copy(posts = remainingPosts)
        }
    }

    fun likePost(songPost: SongPost) {
        viewModelScope.launch {
            repository.likePost(songPost).let {result ->
//                if (result is Resource.Success) fetchSongPosts()
            }
        }
    }

    fun fetchSwipeSongPosts() = viewModelScope.launch {
        val mockPost = MockPost.posts
        songPostState.update {
            it.copy(posts = mockPost)
        }
//        songPostState.update {
//            it.copy(isLoading = true)
//        }
//        repository.fetchPostsFromFollowing().collectLatest { result ->
//            when(result) {
//                is Resource.Success -> {
//                    Log.d("Success", "Fetch following posts from fire store")
//                    result.data?.let {posts ->
//                        songPostState.update {
//                            it.copy(
//                                posts = posts,
//                                isLoading = false
//                            )
//                        }
//                    }
//                }
//                is Resource.Error -> {
//                    songPostState.update {
//                        it.copy(
//                            errorMessage = result.message,
//                            isLoading = false
//                        )
//                    }
//                }
//                is Resource.Loading -> {
//                    songPostState.update {
//                        it.copy(
//                            isLoading = true
//                        )
//                    }
//                }
//            }
//        }
    }
}