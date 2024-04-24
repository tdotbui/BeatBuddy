package edu.temple.beatbuddy

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

// Composable function that displays a list of songs.
// It uses a lazy column which efficiently manages a large list by only recomposing items that are currently visible.
// `songViewModel`: An instance of SongViewModel to manage playback operations.
// `playlist`: The list of songs to display.
@Composable
fun SongList(songViewModel: SongViewModel, playlist: List<Song>) {
    LazyColumn {
        items(playlist) { song ->
            SongCard(songViewModel, song)
        }
    }
}
