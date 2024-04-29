package edu.temple.beatbuddy.music_swipe.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.temple.beatbuddy.component.ImageFactory
import edu.temple.beatbuddy.music_browse.model.mapping.toPlaylistSong
import edu.temple.beatbuddy.music_playlist.view_model.PlaylistViewModel
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.screen.component.UserPostHeader
import edu.temple.beatbuddy.music_swipe.screen.component.SwipeableCard
import edu.temple.beatbuddy.music_swipe.view_model.SwipeSongViewModel
import edu.temple.beatbuddy.user_auth.model.User

@Composable
fun SwipeSongCardScreen(
    swipeSongViewModel: SwipeSongViewModel,
    playlistViewModel: PlaylistViewModel
) {
    LaunchedEffect(Unit) {
        swipeSongViewModel.fetchSwipeSongPosts()
    }
    val posts by swipeSongViewModel.songPostState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (posts.posts.isNotEmpty()) {
            val song = posts.posts.first().toPlaylistSong()
            SwipeableCard(
                onDismiss = {direction ->
                    swipeSongViewModel.removeSongFromList()
                    if (direction == "right") {
                        playlistViewModel.addToFavorite(song)
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(bottom = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    SongCard(
                        songPost = posts.posts.first()
                    )
                }

            }
        } else {
            Text(
                text = "No Recommendations Yet",
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun SongCard(
    songPost: SongPost
) {
    var isPlaying by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserPostHeader(user = songPost.user ?: User())

        ImageFactory(
            context = LocalContext.current,
            imageUrl = songPost.songImage,
            imageVector = Icons.Default.ImageNotSupported,
            description = songPost.title,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = songPost.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = songPost.artistName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "play previous song",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { }
                    .padding(12.dp)
                    .size(32.dp)
            )

            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "pause" else "play",
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onBackground)
                    .clickable {
                        isPlaying = !isPlaying
                    }
                    .size(56.dp)
                    .padding(8.dp)
            )

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "like",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { }
                    .padding(12.dp)
                    .size(32.dp)
            )
        }
    }
}