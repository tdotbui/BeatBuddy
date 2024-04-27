package edu.temple.beatbuddy.music_player.player

import android.annotation.SuppressLint
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CustomPlayer @Inject constructor(
    private val player: ExoPlayer
): Player.Listener {
    var playerState = MutableStateFlow(PlayerState.STATE_IDLE)
        private set

    val currentPlaybackPosition: Long
        get() = if (player.currentPosition > 0) player.currentPosition else 0L

    val currentTrackDuration: Long
        get() = if (player.duration > 0) player.duration else 0L

    fun initPlayer(trackList: MutableList<MediaItem>) {
        player.addListener(this)
        player.setMediaItems(trackList)
        player.prepare()
    }

    fun initSinglePlayer(item: MediaItem) {
        player.addListener(this)
        player.setMediaItem(item)
        player.prepare()
        player.playWhenReady = true
    }

    fun setUpTrack(index: Int, isTrackPlay: Boolean) {
        if (player.playbackState == Player.STATE_IDLE) player.prepare()
        player.seekTo(index, 0)
        if (isTrackPlay) player.playWhenReady = true
    }

    fun playToggle() {
        if (player.playbackState == Player.STATE_IDLE) player.prepare()
        player.playWhenReady = !player.playWhenReady
    }

    fun seekToPosition(position: Long) {
        player.seekTo(position)
    }

    fun releasePlayer() {
        player.release()
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        playerState.tryEmit(PlayerState.STATE_ERROR)
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        if (player.playbackState == Player.STATE_READY) {
            if (playWhenReady) {
                playerState.tryEmit(PlayerState.STATE_PLAYING)
            } else {
                playerState.tryEmit(PlayerState.STATE_PAUSE)
            }
        }
    }

    @SuppressLint("SwitchIntDef")
    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        when (reason) {
            Player.MEDIA_ITEM_TRANSITION_REASON_AUTO -> {
                playerState.tryEmit(PlayerState.STATE_NEXT_SONG)
                playerState.tryEmit(PlayerState.STATE_PLAYING)
            }
//            Player.MEDIA_ITEM_TRANSITION_REASON_SEEK -> {
//                // Emit state for transitioning to the previous track
//                playerState.tryEmit(PlayerState.STATE_PREVIOUS_SONG)
//                playerState.tryEmit(PlayerState.STATE_PLAYING)
//            }
//
//            Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED -> {
//                TODO()
//            }
//
//            Player.MEDIA_ITEM_TRANSITION_REASON_REPEAT -> {
//                TODO()
//            }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        when (playbackState) {
            Player.STATE_IDLE -> playerState.tryEmit(PlayerState.STATE_IDLE)
            Player.STATE_BUFFERING -> playerState.tryEmit(PlayerState.STATE_BUFFERING)
            Player.STATE_READY -> {
                playerState.tryEmit(PlayerState.STATE_READY)
                if (player.playWhenReady) {
                    playerState.tryEmit(PlayerState.STATE_PLAYING)
                } else {
                    playerState.tryEmit(PlayerState.STATE_PAUSE)
                }
            }
            Player.STATE_ENDED -> playerState.tryEmit(PlayerState.STATE_END)
        }
    }
}