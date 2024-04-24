package edu.temple.beatbuddy

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

// ViewModel to manage music playback operations for a specific activity or fragment.
class SongViewModel(context: Context) : ViewModel() {
    // Instance of ExoPlayer initialized with the context provided.
    // This player is responsible for all media playback operations.
    val musicPlayer = ExoPlayer.Builder(context).build()

    // Function to add a song to the player's queue and start playback if not already playing.
    // `song` parameter is an instance of the Song model which contains metadata like title and media URL.
    fun addToQueue(song: Song) {
        Log.d("SongViewModel", "QUEUED: ${song.title}, ${song.artist}")
        musicPlayer.addMediaItem(MediaItem.fromUri(song.media))
        if (!musicPlayer.isPlaying) {
            musicPlayer.prepare()
            musicPlayer.play()
        }
    }
}
