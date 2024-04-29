package edu.temple.beatbuddy.music_player.player

import edu.temple.beatbuddy.music_browse.model.Song
import edu.temple.beatbuddy.music_playlist.model.PlaylistSong

interface PlayerEvent {
    fun onPlayPauseClick()
    fun onPreviousClick()
    fun onNextClick()

    fun onRewindClick()
    fun onForwardClick()
    fun onSongClick(song: PlaylistSong)
    fun onSeekBarPositionChanged(position: Long)
}