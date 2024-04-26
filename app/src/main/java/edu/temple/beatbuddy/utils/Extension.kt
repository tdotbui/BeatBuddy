package edu.temple.beatbuddy.utils

import androidx.media3.common.MediaItem
import edu.temple.beatbuddy.music_browse.model.local.Song
import edu.temple.beatbuddy.music_player.player.CustomPlayer
import edu.temple.beatbuddy.music_player.player.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import edu.temple.beatbuddy.music_player.player.PlaybackState

fun Long.toTime(): String {
    val stringBuffer = StringBuffer()

    val minutes = (this / 60000).toInt()
    val seconds = (this % 60000 / 1000).toInt()

    stringBuffer
        .append(String.format("%02d", minutes))
        .append(":")
        .append(String.format("%02d", seconds))

    return stringBuffer.toString()
}

fun List<Song>.toMediaItemList(): MutableList<MediaItem> = this.map { MediaItem.fromUri(it.preview) }.toMutableList()

fun CoroutineScope.collectPlayerState(
    player: CustomPlayer, updateState: (PlayerState) -> Unit
) = launch {
    player.playerState.collect {
        updateState(it)
    }
}

fun CoroutineScope.launchPlaybackStateJob(
    playbackStateFlow: MutableStateFlow<PlaybackState>, state: PlayerState, player: CustomPlayer
) = launch {
    do {
        playbackStateFlow.emit(
            PlaybackState(
                currentPlaybackPosition = player.currentPlaybackPosition,
                currentTrackDuration = player.currentTrackDuration
            )
        )
        delay(1000) // delay for 1 second
    } while (state == PlayerState.STATE_PLAYING && isActive)
}