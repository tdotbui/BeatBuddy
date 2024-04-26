package edu.temple.beatbuddy.music_player.view_model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.music_browse.model.local.Song
import edu.temple.beatbuddy.music_player.player.CustomPlayer
import edu.temple.beatbuddy.music_player.player.PlaybackState
import edu.temple.beatbuddy.music_player.player.PlayerEvent
import edu.temple.beatbuddy.music_player.player.PlayerState
import edu.temple.beatbuddy.utils.collectPlayerState
import edu.temple.beatbuddy.utils.launchPlaybackStateJob
import edu.temple.beatbuddy.utils.toMediaItemList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    val player: CustomPlayer,
) : ViewModel(), PlayerEvent {

    var currentSongList = mutableStateListOf<Song>()
        private set

    var selectedSong: Song? by mutableStateOf(null)
        private set

    var selectedSongIndex: Int by mutableStateOf(-1)

    private var isPlaying = mutableStateOf(false)

    private var playbackStateJob: Job? = null

    var playbackState = MutableStateFlow(PlaybackState(0L, 0L))
        private set

    private var isAuto: Boolean = false

    fun setUpSongLists(songList: List<Song>) {
        player.initPlayer(songList.toMediaItemList())
        observePlayerState()
    }

    private fun onSongSelected(index: Int) {
        if (selectedSongIndex == -1) isPlaying.value = true
        if (selectedSongIndex == -1 || selectedSongIndex != index) {
            selectedSongIndex = index
            setUpTrack()
        }
    }

    private fun setUpTrack() {
        if (!isAuto) player.setUpTrack(selectedSongIndex, isPlaying.value)
        isAuto = false
    }

    private fun updateState(state: PlayerState) {
        if (selectedSongIndex != -1) {
            isPlaying.value = state == PlayerState.STATE_PLAYING
            selectedSong = currentSongList[selectedSongIndex]

            updatePlaybackState(state)
            if (state == PlayerState.STATE_NEXT_TRACK) {
                isAuto = true
                onNextClick()
            }
            if (state == PlayerState.STATE_END) onSongSelected(0)
        }
    }
    private fun observePlayerState() = viewModelScope.collectPlayerState(player, ::updateState)

    private fun updatePlaybackState(state: PlayerState) {
        playbackStateJob?.cancel()
        playbackStateJob = viewModelScope.launchPlaybackStateJob(playbackState, state, player)
    }

    override fun onPlayPauseClick() = player.playPause()

    override fun onPreviousClick() {
        if (selectedSongIndex > 0) onSongSelected(selectedSongIndex - 1)
    }

    override fun onNextClick() {
        if (selectedSongIndex < currentSongList.size - 1) onSongSelected(selectedSongIndex + 1)
    }

    override fun onRewindClick() {
        viewModelScope.launch {
            val position = player.currentPlaybackPosition - 5000L
            if (position > 0) player.seekToPosition(position)
            else player.seekToPosition(0L)
        }
    }

    override fun onForwardClick() {
        viewModelScope.launch {
            val position = player.currentPlaybackPosition + 5000L
            if (position < player.currentTrackDuration) player.seekToPosition(position)
            else player.seekToPosition(0L)
        }
    }

    override fun onSongClick(song: Song) = onSongSelected(currentSongList.indexOf(song))

    override fun onSeekBarPositionChanged(position: Long) {
        viewModelScope.launch {
            player.seekToPosition(position)
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.releasePlayer()
    }
}