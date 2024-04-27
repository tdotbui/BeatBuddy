package edu.temple.beatbuddy.music_player.view_model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    val player: CustomPlayer,
) : ViewModel(), PlayerEvent {

    private val _currentSongList = mutableStateListOf<Song>()
    val currentSongList: List<Song> get() = _currentSongList

    var selectedSong = MutableStateFlow<Song?>(null)
        private set

    var selectedSongIndex: Int by mutableStateOf(-1)

    var isPlaying = MutableStateFlow(false)
        private set

    var isFullScreen = MutableStateFlow(false)
        private set

    private var isAuto: Boolean = false

    private var playbackStateJob: Job? = null

    private val _playbackState = MutableStateFlow(PlaybackState(0L, 0L))
    val playbackState: StateFlow<PlaybackState> get() = _playbackState

    init {
        viewModelScope.launch {
            player.playerState.collect { state ->
                if (state == PlayerState.STATE_NEXT_SONG) onNextTrack()
            }
        }
        observePlayerState()
    }

    private fun updatePlaybackState(state: PlayerState) {
        playbackStateJob?.cancel()
        playbackStateJob = viewModelScope.launchPlaybackStateJob(_playbackState, state, player)
    }

    private fun observePlayerState() {
        viewModelScope.collectPlayerState(player, ::updateState)
    }

    private fun updateState(state: PlayerState) {
        if (selectedSongIndex != -1) {
            isPlaying.value = state == PlayerState.STATE_PLAYING

            updatePlaybackState(state)
//            if (state == STATE_NEXT_TRACK) {
//                isAuto = true
//                onNextClick()
//            }
            if (state == PlayerState.STATE_END) onSongSelected(0)
        }
    }

    fun getCurrentTrackDuration() = player.currentTrackDuration

    fun setUpSongLists(songList: List<Song>) {
        _currentSongList.addAll(songList)
        player.initPlayer(songList.toMediaItemList())
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

    private fun onNextTrack() {
        if (selectedSongIndex < currentSongList.size - 1) {
            isAuto = true
            selectedSongIndex++
            selectedSong.value = _currentSongList[selectedSongIndex]
        }
    }

    fun minimizeScreen() {
        isFullScreen.value = false
    }

    fun maximizeScreen () {
        isFullScreen.value = true
    }

    override fun onPlayPauseClick() {
        player.playToggle()
//        isPlaying.value = !isPlaying.value
    }

    override fun onPreviousClick() {
        if (selectedSongIndex > 0) {
            onSongSelected(selectedSongIndex - 1)
            selectedSong.value = _currentSongList[selectedSongIndex]
        }
    }

    override fun onNextClick() {
        if (selectedSongIndex < currentSongList.size - 1) {
            onSongSelected(selectedSongIndex + 1)
            selectedSong.value = _currentSongList[selectedSongIndex]
        }
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

    override fun onSongClick(song: Song) {
        selectedSong.value = song
        onSongSelected(index = _currentSongList.indexOf(song))
    }

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