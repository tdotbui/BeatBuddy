package edu.temple.beatbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import edu.temple.beatbuddy.ui.theme.BeatBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val songViewModel = SongViewModel(this)

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

        setContent {
            BeatBuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SongList(songViewModel, playlist)
                    MediaController(songViewModel)
                }
            }
        }
    }
}
