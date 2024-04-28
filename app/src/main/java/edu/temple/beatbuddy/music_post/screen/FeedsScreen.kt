package edu.temple.beatbuddy.music_post.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import edu.temple.beatbuddy.music_player.view_model.SongViewModel
import edu.temple.beatbuddy.music_post.screen.component.SongPostItem
import edu.temple.beatbuddy.music_post.view_model.SongPostViewModel

@Composable
fun FeedsScreen(
    songPostViewModel: SongPostViewModel,
    songViewModel: SongViewModel
) {
    val posts by songPostViewModel.songPostState.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            songPostViewModel.fetchSongPosts()
            songPostViewModel.clearCurrentSongPost()
            if (songViewModel.isPlaying.value) songViewModel.onPlayPauseClick()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (posts.posts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No posts yet",
                    fontSize = 36.sp,
                    color = Color.Gray
                )
            }
        } else {
            if (posts.isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    items(posts.posts.size) { index ->
                        SongPostItem(
                            songPost = posts.posts[index],
                            songPostViewModel = songPostViewModel,
                            songViewModel = songViewModel
                        )
                    }
                }
            }
        }
    }
}