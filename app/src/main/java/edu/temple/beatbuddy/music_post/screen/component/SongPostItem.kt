package edu.temple.beatbuddy.music_post.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.automirrored.outlined.InsertComment
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.temple.beatbuddy.component.ImageFactory
import edu.temple.beatbuddy.component.VinylAlbumCoverAnimation
import edu.temple.beatbuddy.music_player.view_model.SongViewModel
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.view_model.SongPostViewModel

@Composable
fun SongPostItem(
    songPost: SongPost,
    songPostViewModel: SongPostViewModel,
    songViewModel: SongViewModel,
) {
    val context = LocalContext.current
    val user = songPost.user

    var didLike by remember { mutableStateOf(songPost.didLike) }
    var likes by remember { mutableIntStateOf(songPost.likes) }

    val isPlaying by songViewModel.isPlaying.collectAsState()

    val currentSong by songPostViewModel.currentSongPost.collectAsState()
    val imageOpacity = if (isPlaying && currentSong == songPost) 1f else 0.5f

    Card(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .clickable {

            },
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            if (user != null) {
                UserPostHeader(user = user)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (isPlaying && currentSong == songPost) {
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
                                songViewModel.onPlayPauseClick()
                            },
                    )
                } else {
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
                                if (!isPlaying) songViewModel.onPlayPauseClick()
                                songPostViewModel.setCurrentSongPost(songPost)
                                songViewModel.setUpSongPost(songPost)
                            }
                    )
                }
            }

            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user?.username ?: "username",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )

                val caption = songPost.caption
                caption?.run {
                    Text(
                        text = "  $caption",
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp
                    )
                }
            }

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (didLike == true) Icons.Default.ThumbUp else Icons.Default.ThumbUpOffAlt,
                        contentDescription = null,
                        modifier = Modifier
                            .size(17.dp)
                            .clickable {
                                didLike = didLike != true
                                if (didLike == true) {
                                    songPostViewModel.likePost(songPost)
                                    likes++
                                } else {
                                    songPostViewModel.unlikePost(songPost)
                                    likes--
                                }
                            }
                    )
                    if (likes > 0) {
                        Text(
                            text = "$likes",
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.InsertComment,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SongPostPV() {
//    SongPostItem(
//        songPost = MockPost.posts[2],
//        player = ExoPlayer.Builder(LocalContext.current).build(),
//        {},
//        songPostViewModel = hiltViewModel()
//    )
//}