package edu.temple.beatbuddy.music_post.screen.component

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import edu.temple.beatbuddy.app.screen.HomeScreen
import edu.temple.beatbuddy.component.ImageFactory
import edu.temple.beatbuddy.component.VinylAlbumCover
import edu.temple.beatbuddy.component.VinylAlbumCoverAnimation
import edu.temple.beatbuddy.discover.screen.component.ProfilePicture
import edu.temple.beatbuddy.music_post.model.MockPost
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.view_model.SongPostItemViewModel
import edu.temple.beatbuddy.music_post.view_model.SongPostViewModel
import edu.temple.beatbuddy.utils.ImageSize
import kotlinx.coroutines.launch

@Composable
fun SongPostItem(
    songPost: SongPost,
    player: ExoPlayer,
    songPostViewModel: SongPostViewModel,
    songPostItemViewModel: SongPostItemViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val user = songPost.user
    val didLike by remember { mutableStateOf(songPost.didLike) }

    var isPlaying by remember { mutableStateOf(false) }

    val imageOpacity = if (isPlaying) 1f else 0.5f

    val playerView = PlayerView(context)
    val playWhenReady by rememberSaveable { mutableStateOf(true) }

    val currentSong by songPostViewModel.currentSongPost.collectAsState()

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

                    (user?.username ?: user?.email)?.let {
                        Text(
                            text = it,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
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
                                songPostViewModel.setCurrentSong(songPost)
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
                (user?.username ?: user?.email)?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.SemiBold
                    )
                }

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
                        imageVector = if (didLike == true) Icons.Default.ThumbUp else Icons.Default.ThumbUpOffAlt,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                if (didLike == false) scope.launch { songPostItemViewModel.like(songPost) }
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

@Preview(showBackground = true)
@Composable
fun SongPostPV() {
    SongPostItem(
        songPost = MockPost.posts[2],
        player = ExoPlayer.Builder(LocalContext.current).build(),
        songPostViewModel = hiltViewModel(),
        songPostItemViewModel = hiltViewModel()
    )
}