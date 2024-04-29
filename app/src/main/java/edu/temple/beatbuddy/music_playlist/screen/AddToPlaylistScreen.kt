package edu.temple.beatbuddy.music_playlist.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ImageSearch
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.temple.beatbuddy.component.ImageFactory
import edu.temple.beatbuddy.music_browse.model.Song
import edu.temple.beatbuddy.music_playlist.model.Playlist
import edu.temple.beatbuddy.music_playlist.screen.component.PlaylistRowItem
import edu.temple.beatbuddy.music_playlist.view_model.PlaylistViewModel

@Composable
fun AddToPlaylistScreen(
    playlistViewModel: PlaylistViewModel,
    finish: () -> Unit
) {
    var playlistName by remember { mutableStateOf("Enter new playlist") }
    val playlists by playlistViewModel.playlistState.collectAsState()
    var setUpPlaylist by remember { mutableStateOf(Playlist(name = "")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Cancel",
                fontSize = 14.sp,
                modifier = Modifier.clickable { finish() }
            )

            Text(
                text = "Add to playlist",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )

            Text(
                text = "Done",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable {
                        if (playlistName != setUpPlaylist.name) {
                            setUpPlaylist.id = 0L
                            setUpPlaylist.name = playlistName
                            setUpPlaylist.imageUrl = playlistViewModel.currentSong.value.songImage
                            playlistViewModel.insertSong(setUpPlaylist)
                        } else {
                            playlistViewModel.insertSong(setUpPlaylist)
                        }
                        finish()
                    }
            )
        }

        Divider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier
                        .size(32.dp)
                )
            }

            Column(
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                BasicTextField(
                    value = playlistName,
                    onValueChange = { playlistName = it },
                    textStyle = MaterialTheme.typography.bodyLarge
                )
                Divider()
            }
        }

        Text(
            text = "-Or-",
            fontSize = 12.sp,
            fontWeight = FontWeight.Light
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "All playlists",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Divider()

            LazyColumn {
                items(playlists.playlists.size) {
                    val playlist = playlists.playlists[it]
                    PlaylistRowItem(playlist = playlist) { id, name ->
                        playlistName = name
                        setUpPlaylist = playlist
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun AddPlaylistPV() {
//    AddToPlaylistScreen()
//}