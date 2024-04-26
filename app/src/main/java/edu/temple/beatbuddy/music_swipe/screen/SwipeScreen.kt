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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import edu.temple.beatbuddy.component.ImageFactory
import edu.temple.beatbuddy.component.VinylAlbumCoverAnimation
import edu.temple.beatbuddy.discover.screen.component.ProfilePicture
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.screen.component.SongPostItem
import edu.temple.beatbuddy.music_post.view_model.SongPostViewModel
import edu.temple.beatbuddy.music_swipe.view_model.SwipeSongViewModel
import edu.temple.beatbuddy.utils.ImageSize

@Composable
fun SwipeScreen(
    swipeSongViewModel: SwipeSongViewModel,
    player: ExoPlayer
) {
    val posts by swipeSongViewModel.songPostState.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            if (player.isPlaying) player.stop()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn {
            items(posts.posts.size) { index ->
                TempSongPostItem(
                    songPost = posts.posts[index],
                    player = player,
                    likePost = { },
                    swipeSongViewModel = swipeSongViewModel
                )
            }
        }

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
        }
    }
}

@Composable
fun TempSongPostItem(
    songPost: SongPost,
    player: ExoPlayer,
    likePost: () -> Unit,
    swipeSongViewModel: SwipeSongViewModel
) {
    val context = LocalContext.current
    val user = songPost.user

    var didLike by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }

    val imageOpacity = if (isPlaying) 1f else 0.5f

    val playerView = PlayerView(context)
    val playWhenReady by rememberSaveable { mutableStateOf(true) }

    val currentSong by swipeSongViewModel.currentSongPost.collectAsState()

    LaunchedEffect(currentSong) {
        if (currentSong?.postId != songPost.postId) {
            isPlaying = false
        }
    }

    Card(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfilePicture(
                        userProfile = songPost.user?.profileImage ?: "",
                        size = ImageSize.xSmall
                    )

                    Text(
                        text = user?.username ?: "username",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (!isPlaying) {
                    ImageFactory(
                        context = context,
                        imageUrl = songPost.songImage,
                        imageVector = Icons.Default.ImageNotSupported,
                        description = songPost.title,
                        modifier = Modifier
                            .size(250.dp)
                            .alpha(imageOpacity)
                    )

                    Icon(
                        imageVector = Icons.Default.PlayCircleOutline,
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .alpha(0.3f)
                            .clickable {
                                swipeSongViewModel.setCurrentSong(songPost)
                                player.setMediaItem(MediaItem.fromUri(songPost.preview))
                                playerView.player = player
                                isPlaying = true
                            }
                    )
                } else {
                    VinylAlbumCoverAnimation(
                        imageUrl = songPost.songImage,
                        isPlaying = isPlaying
                    )

                    Icon(
                        imageVector = Icons.Default.PauseCircleOutline,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(120.dp)
                            .alpha(0.5f)
                            .clickable {
                                player.pause()
                                isPlaying = false
                            },
                    )

                    LaunchedEffect(player) {
                        player.prepare()
                        player.playWhenReady = playWhenReady
                    }
                }
            }

            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = user?.username ?: "username",
                    fontWeight = FontWeight.SemiBold
                )

                val caption = songPost.caption
                caption?.run {
                    Text(
                        text = "  $caption",
                        fontWeight = FontWeight.Light,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (didLike) Icons.Default.ThumbUp else Icons.Default.ThumbUpOffAlt,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                didLike = !didLike
                                likePost()
                            }
                    )

                    Text(text = "${songPost.likes}")
                }

                Icon(
                    imageVector = Icons.Default.Comment,
                    contentDescription = null
                )

                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null
                )
            }
        }
    }
}