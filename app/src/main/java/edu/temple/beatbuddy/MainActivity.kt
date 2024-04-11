package edu.temple.beatbuddy

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import edu.temple.beatbuddy.ui.theme.BeatBuddyTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playlist: List<Song> = listOf(
            Song(
                title = "Incredible (feat. Labrinth)",
                artist = "Sia",
                media = "https://cdns-preview-7.dzcdn.net/stream/c-792912511f37c14d55918e5f6db232a7-1.mp3"
            ),
            Song(
                title = "Single Soon",
                artist = "Selena Gomez",
                media = "https://cdns-preview-7.dzcdn.net/stream/c-70c8502cd24b1c54e6a8a5d22238f60c-2.mp3"
            ),
            Song(
                title = "The Show",
                artist = "Niall Horan",
                media = "https://cdns-preview-e.dzcdn.net/stream/c-eb83feaa48488c9fd4d5d80ae217ac6d-6.mp3"
            ),
            Song(
                title = "We can't be friends (wait for your love)",
                artist = "Ariana Grande",
                media = "https://cdns-preview-e.dzcdn.net/stream/c-ee20b2df47cfce93945821c121f9716c-5.mp3"
            )
        )

        val musicPlayer = ExoPlayer.Builder(this).build()

        setContent {
            BeatBuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SongList(musicPlayer, playlist)
                    MediaController(musicPlayer)
                }
            }
        }
    }
}

@Composable
fun SongList(player: ExoPlayer, playlist: List<Song>) {
    LazyColumn {
        items(playlist) { song ->
            SongCard(player, song)
        }
    }
}

@Composable
fun SongCard(player: ExoPlayer ,song: Song) {
    val context = LocalContext.current

    val playerView = PlayerView(context)
    val playWhenReady by rememberSaveable { mutableStateOf(true) }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Column {
                Text(song.title)
                Text(song.artist)
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                Log.d("SongCard", "Queue pressed")

                player.setMediaItem(MediaItem.fromUri(song.media))
                playerView.player = player
            }) {
                Text("Queue")


                LaunchedEffect(player) {
                    player.prepare()
                    player.playWhenReady = playWhenReady
                }
            }
        }
    }
}

@OptIn(UnstableApi::class) @Composable
fun MediaController(player: ExoPlayer) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(onClick = {
            Log.d("MediaController", "Backward pressed")
            if (player.hasPreviousMediaItem()) {
                Log.d("MediaController", "Going to previous song")
                player.seekToPreviousMediaItem()
            } else {
                Log.d("MediaController", "Restarting current song")
                player.seekToPrevious()
            }
        }) {
            Text("Backward")
        }
        Button(onClick = {
            if (player.currentMediaItem != null) {
                if (player.isPlaying) {
                    Log.d("MediaController", "Pause pressed")
                    player.pause()
                } else {
                    Log.d("MediaController", "Play pressed")
                    player.prepare()
                    player.play()
                }
            } else {
                Log.d("MediaController", "No media item to play")
            }
        }) {
            Text("Play/Pause")
        }
        Button(onClick = {
            Log.d("MediaController", "Forward pressed")
            if (player.hasNextMediaItem()) {
                Log.d("MediaController", "Going to next song")
                player.seekToNextMediaItem()
            } else {
                Log.d("MediaController", "No next song in queue")
            }
        }) {
            Text("Forward")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BeatBuddyTheme {
    }
}