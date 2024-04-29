package edu.temple.beatbuddy.music_post.screen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.temple.beatbuddy.component.VinylAlbumCover
import edu.temple.beatbuddy.music_post.model.MockPost
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.view_model.SongPostViewModel

@Composable
fun SongPostRowItem(
    songPost: SongPost,
    songPostViewModel: SongPostViewModel,
    onLongPress: (SongPost) -> Unit
) {
    var didLike by remember { mutableStateOf(songPost.didLike) }
    var likes by remember { mutableIntStateOf(songPost.likes) }

    Card(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onLongPress(songPost) },
                    onTap = {}
                )
            },
        border = BorderStroke(1.dp, Color.White),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            pressedElevation = 20.dp
        ),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            VinylAlbumCover(imageUrL = songPost.songImage)

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.9f),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = songPost.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )

                    Row(
                        modifier = Modifier
                            .wrapContentSize(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (likes > 0) {
                            Text(
                                text = "$likes",
                                fontWeight = FontWeight.ExtraLight,
                                fontSize = 12.sp
                            )
                        }

                        Icon(
                            imageVector = if (didLike == true) Icons.Default.ThumbUp else Icons.Default.ThumbUpOffAlt,
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                    didLike = didLike != true
                                    if (didLike == true) {
                                        songPostViewModel.likePost(songPost)
                                        likes++
                                    } else {
                                        songPostViewModel.unlikePost(songPost)
                                        likes--
                                    }
                                    songPostViewModel.fetchSongPosts()
                                }
                        )

                    }
                }
                Divider()

                songPost.caption?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Light,
                        fontSize = 10.sp,
                        maxLines = 2,
                        lineHeight = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SongPostRowPV() {
//    SongPostRowItem(
//        songPost = MockPost.posts[2]
//    )
//}