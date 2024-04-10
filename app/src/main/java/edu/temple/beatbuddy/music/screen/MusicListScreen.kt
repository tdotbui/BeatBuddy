package edu.temple.beatbuddy.music.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import edu.temple.beatbuddy.music.screen.component.SongRowItem
import edu.temple.beatbuddy.music.view_model.SongListViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MusicListScreen(
    navController: NavHostController,
    songListViewModel: SongListViewModel = hiltViewModel()
) {
    if (songListViewModel.songListState.value.currentSongList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
        ) {
            items(songListViewModel.songListState.value.currentSongList.size) {
                index ->

                SongRowItem(
                    song = songListViewModel.songListState.value.currentSongList[index],
                    navHostController = navController
                )
            }
        }
    }
}