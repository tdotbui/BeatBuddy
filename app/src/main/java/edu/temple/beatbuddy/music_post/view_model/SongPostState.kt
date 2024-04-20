package edu.temple.beatbuddy.music_post.view_model

import edu.temple.beatbuddy.music_post.model.SongPost

data class SongPostState(
    val isLoading: Boolean = false,

    val posts: List<SongPost> = emptyList(),

    val errorMessage: String? = null
)
