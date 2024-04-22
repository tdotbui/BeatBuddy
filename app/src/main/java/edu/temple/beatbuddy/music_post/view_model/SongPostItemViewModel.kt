package edu.temple.beatbuddy.music_post.view_model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.repository.SongPostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongPostItemViewModel  @Inject constructor(
    private val repository: SongPostRepository
): ViewModel() {

//    var didLike = MutableStateFlow(false)
//        private set
    suspend fun like(songPost: SongPost) {
        songPost.didLike = true
        try {
            songPost.likes++
            repository.likePost(songPost)
        } catch (e: Exception) {
            // Handle exception
        }
    }
}