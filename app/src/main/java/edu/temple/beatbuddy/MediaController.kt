package edu.temple.beatbuddy

import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi

// Media controller UI component for managing playback operations.
// This component provides a row of buttons for controlling the music player (back, play/pause, forward, clear queue).
@OptIn(UnstableApi::class) @Composable
fun MediaController(songViewModel: SongViewModel) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        // Button to go to the previous song or restart the current song if it's the first in the playlist.
        Button(onClick = {
            Log.d("MediaController", "BACKWARD")
            if (songViewModel.musicPlayer.hasPreviousMediaItem()) {
                Log.d("MediaController", "Going to previous song")
                songViewModel.musicPlayer.seekToPreviousMediaItem()
            } else {
                Log.d("MediaController", "Restarting current song")
                songViewModel.musicPlayer.seekToPrevious()
            }
        }) {
            Text("Backward")
        }

        // Button to toggle play and pause based on the current playback state.
        Button(onClick = {
            if (songViewModel.musicPlayer.currentMediaItem != null) {
                if (songViewModel.musicPlayer.isPlaying) {
                    Log.d("MediaController", "PAUSED")
                    songViewModel.musicPlayer.pause()
                } else {
                    Log.d("MediaController", "PLAYING: ${songViewModel.musicPlayer.currentMediaItem!!.mediaId}")
                    songViewModel.musicPlayer.prepare()
                    songViewModel.musicPlayer.play()
                }
            } else {
                Log.d("MediaController", "No media item to play")
            }
        }) {
            Text("Play/Pause")
        }

        // Button to advance to the next song in the playlist, if available.
        Button(onClick = {
            Log.d("MediaController", "FORWARD")
            if (songViewModel.musicPlayer.hasNextMediaItem()) {
                Log.d("MediaController", "Going to next song")
                songViewModel.musicPlayer.seekToNextMediaItem()
            } else {
                Log.d("MediaController", "No next song in queue")
            }
        }) {
            Text("Forward")
        }

        // Button to clear the entire queue of songs.
        Button(onClick = {
            Log.d("MediaController", "QUEUE CLEARED")
            songViewModel.musicPlayer.clearMediaItems()
        }) {
            Text("Clear")
        }
    }
}
