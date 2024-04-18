package edu.temple.beatbuddy.music.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import edu.temple.beatbuddy.music.model.local.Song

@Composable
fun SongRowItem(
    song: Song,
    onMusicClick: () -> Unit,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(song.album.cover_medium)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Divider()

        Box(
            modifier = Modifier
                .wrapContentSize(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { onMusicClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageState.painter != null) {
                        Image(
                            painter = imageState.painter!!,
                            contentDescription = song.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(MaterialTheme.shapes.medium)
                        )
                    } else {
                        Icon(
                            modifier = Modifier.size(56.dp),
                            imageVector = Icons.Rounded.ImageNotSupported,
                            contentDescription = "\"${song.title}\" track cover"
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(5f)
                        .padding(bottom = 4.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = song.title,
                        maxLines = 1,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(end = 16.dp)
                    )

                    Text(
                        text = song.artist.name!!,
                        maxLines = 1,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onClick() }
            )
        }
    }
}