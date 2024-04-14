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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
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
    navHostController: NavHostController
) {
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(song.album.cover_medium)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable {
//                navHostController.navigate()
            }
    ) {
        if (imageState is AsyncImagePainter.State.Success) {
            Image(
                painter = imageState.painter,
                contentDescription = "\"${song.title}\" track cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }

        if (imageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
                    .width(80.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = "\"${song.title}\" track cover"
                )
            }
        }

        Column(
            modifier = Modifier.padding(start = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = song.title,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                maxLines = 1
            )

            Text(
                text = song.artist.name,
                color = Color.Black,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
                maxLines = 1
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Navigate",
                color = Color.Black,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
    }
}