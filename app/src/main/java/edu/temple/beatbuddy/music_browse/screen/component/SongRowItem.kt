package edu.temple.beatbuddy.music_browse.screen.component

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import edu.temple.beatbuddy.music_browse.model.Song
import edu.temple.beatbuddy.music_playlist.model.PlaylistSong

@Composable
fun SongRowItem(
    song: PlaylistSong,
    onMusicClick: (PlaylistSong) -> Unit,
    shareClick: (PlaylistSong) -> Unit,
    addClick: (PlaylistSong) -> Unit
) {
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(song.songImage)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(bottom = 8.dp)
            .clickable { onMusicClick(song) },
        elevation = CardDefaults.cardElevation(5.dp),
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = Color.White
                )
                .padding(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { onMusicClick(song) },
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
                        text = song.artistName,
                        maxLines = 1,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { expanded = true }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    MenuItem("Play") {
                        onMusicClick(song)
                        expanded = false
                    }
                    MenuItem("Add to Playlist") {
                        addClick(song)
                        expanded = false
                    }
                    MenuItem("Share") {
                        shareClick(song)
                        expanded = false
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItem(text: String, onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(text = text) }, 
        onClick = { onClick() }
    )
}