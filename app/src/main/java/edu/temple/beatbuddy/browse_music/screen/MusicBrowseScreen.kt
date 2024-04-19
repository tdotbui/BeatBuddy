package edu.temple.beatbuddy.browse_music.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.temple.beatbuddy.browse_music.screen.component.GenreItem
import edu.temple.beatbuddy.browse_music.screen.component.SongRowItem
import edu.temple.beatbuddy.browse_music.view_model.SongListViewModel
import edu.temple.beatbuddy.utils.Genre

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MusicBrowseScreen(
    songListViewModel: SongListViewModel = hiltViewModel()
) {
    val songs by songListViewModel.songListState.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyRow(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            userScrollEnabled = true
        ) {
            items(Genre.entries.size) { index ->
                val genre = Genre.entries[index]
                GenreItem(
                    genre = genre,
                    onClick = { songListViewModel.getSongsByGenre(genre.id) }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
            ) {
                val songList = songs.currentSongList
                items(songList.size) { index ->
                    SongRowItem(
                        onClick = {},
                        song = songList[index],
                        onMusicClick = {}
                    )
                }
            }

            if (songs.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}