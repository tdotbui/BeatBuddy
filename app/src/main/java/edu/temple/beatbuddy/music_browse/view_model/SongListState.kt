package edu.temple.beatbuddy.music_browse.view_model

import edu.temple.beatbuddy.music_browse.model.local.Song
import edu.temple.beatbuddy.utils.Genre

data class SongListState(
    val isLoading: Boolean = false,

    val selectedGenre: Int = Genre.POP.id,
    val currentSongList: List<Song> = emptyList(),

    val errorMessage: String? = null
)