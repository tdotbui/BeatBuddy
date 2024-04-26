package edu.temple.beatbuddy.music_player.player

import edu.temple.beatbuddy.music_browse.model.local.Song

interface PlayerEvent {
    fun onPlayPauseClick()
    fun onPreviousClick()
    fun onNextClick()

    fun onRewindClick()
    fun onForwardClick()
    fun onSongClick(song: Song)
    fun onSeekBarPositionChanged(position: Long)
}