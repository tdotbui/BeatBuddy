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
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.temple.beatbuddy.component.ImageFactory
import edu.temple.beatbuddy.component.VinylAlbumCover
import edu.temple.beatbuddy.component.VinylAlbumCoverAnimation
import edu.temple.beatbuddy.music_browse.model.mapping.toPlaylistSong
import edu.temple.beatbuddy.music_player.view_model.SongViewModel
import edu.temple.beatbuddy.music_playlist.view_model.PlaylistViewModel
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.screen.component.UserPostHeader
import edu.temple.beatbuddy.music_post.view_model.SongPostViewModel
import edu.temple.beatbuddy.music_swipe.screen.component.SwipeableCard
import edu.temple.beatbuddy.music_swipe.sensor.SensorHandler
import edu.temple.beatbuddy.music_swipe.view_model.SwipeSongViewModel
import edu.temple.beatbuddy.ui.theme.Pink40
import edu.temple.beatbuddy.ui.theme.Pink80
import edu.temple.beatbuddy.user_auth.model.User

@Composable
fun SwipeSongCardScreen(
    swipeSongViewModel: SwipeSongViewModel,
    playlistViewModel: PlaylistViewModel,
    songViewModel: SongViewModel,
    songPostViewModel: SongPostViewModel
) {

    LaunchedEffect(Unit) {
        swipeSongViewModel.fetchSwipeSongPosts()
    }

    val context = LocalContext.current
    val sensorHandler = remember { SensorHandler(context, swipeSongViewModel) }

    DisposableEffect(key1 = sensorHandler) {
        sensorHandler.register()
        onDispose {
            sensorHandler.unregister()
        }
    }

    val posts by swipeSongViewModel.songPostState.collectAsState()
    val playListPost = posts.posts.map {
        it.toPlaylistSong()
    }

    val isDiscovering by songViewModel.isDiscovering.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            if (isDiscovering) {
                songViewModel.clearSongList()
                songViewModel.stop()
            }
            songViewModel.discoverNow(false)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (posts.posts.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                SwipeableCard(
                    onDismiss = {direction ->
                        swipeSongViewModel.removeSongFromList()
                        if (direction == "right") {
                            val song = posts.posts.toMutableList().removeFirst().toPlaylistSong()
                            playlistViewModel.addToFavorite(song)
                            songPostViewModel.fetchSongPosts()
                        }
                        songViewModel.onNextClick()
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .alpha(if (isDiscovering) 1f else 0.3f)
                            .padding(16.dp)
                            .padding(bottom = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        SongCard(
                            songPost = posts.posts.first(),
                            songViewModel = songViewModel,
                            playToggle = {
                                songViewModel.onPlayPauseClick()
                            }
                        )
                    }
                }

                if (!isDiscovering) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .clickable {
                                songViewModel.setUpSongLists(playListPost)
                                songViewModel.onSongClick(posts.posts.first().toPlaylistSong())
                                songViewModel.discoverNow(true)
                            },
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Discover Hot Pick Today",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Pink40
                        )

                        Icon(
                            imageVector = Icons.Default.Whatshot,
                            contentDescription = "",
                            modifier = Modifier.size(96.dp),
                            tint = Pink40
                        )
                    }
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
    songPost: SongPost,
    songViewModel: SongViewModel,
    playToggle: () -> Unit
) {
    val isPlaying by songViewModel.isPlaying.collectAsState()
    val currentSong by songViewModel.selectedSong.collectAsState()
    val isDiscovering by songViewModel.isDiscovering.collectAsState()

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserPostHeader(user = songPost.user ?: User())
        
        if (isPlaying && currentSong.id == songPost.songId) {
            VinylAlbumCoverAnimation(imageUrl = songPost.songImage)
        } else {
            VinylAlbumCover(
                imageUrL = songPost.songImage,
                modifier = Modifier.alpha(0.7f)
            )
        }

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
                contentDescription = "Dislike",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { }
                    .padding(12.dp)
                    .size(32.dp)
            )

            Icon(
                imageVector = if (isPlaying && isDiscovering) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying && isDiscovering) "pause" else "play",
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onBackground)
                    .clickable {
                        playToggle()
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