package edu.temple.beatbuddy.music_player.player

enum class PlayerState {
    STATE_IDLE,
    STATE_READY,
    STATE_BUFFERING,
    STATE_ERROR,
    STATE_END,
    STATE_PLAYING,
    STATE_PAUSE,
    STATE_NEXT_TRACK,
}

data class PlaybackState(
    val currentPlaybackPosition: Long,
    val currentTrackDuration: Long
)