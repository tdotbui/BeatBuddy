package edu.temple.beatbuddy.music_player.view_model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.music_browse.model.local.Song
import edu.temple.beatbuddy.music_player.model.SongEvent
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    val player: Player,
) : ViewModel() {

    var currentSong: MutableState<Song?> = mutableStateOf(null)
        private set

    fun play(song: Song) {
        player.setMediaItem(
            MediaItem.fromUri(song.preview)
        )
        currentSong.value = song
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
        currentSong.value = null
    }
}