package edu.temple.beatbuddy

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import edu.temple.beatbuddy.ui.theme.BeatBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playlist: List<Song> = listOf(
            Song(
                title = "Master of Puppets",
                artist = "Metallica",
                media = R.raw.master_of_puppets
            ),
            Song(
                title = "Everyday Normal Guy 2",
                artist = "Jon Lajoie",
                media = R.raw.everyday_normal_guy_2
            ),
            Song(
                title = "Lose Yourself",
                artist = "Eminem",
                media = R.raw.lose_yourself
            ),
            Song(
                title = "Crazy",
                artist = "Gnarls Barkley",
                media = R.raw.crazy
            ),
            Song(
                title = "Till I Collapse",
                artist = "Eminem",
                media = R.raw.till_i_collapse
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
    val packageName = context.packageName

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
                player.addMediaItem(MediaItem.fromUri("android.resource://$packageName/${song.media}"))
            }) {
                Text("Queue")
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