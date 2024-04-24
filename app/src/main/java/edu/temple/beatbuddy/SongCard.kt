package edu.temple.beatbuddy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Composable function that displays a single song in a card view.
// `songViewModel`: An instance of SongViewModel to handle song queue operations.
// `song`: The song data that will be displayed in this card.
@Composable
fun SongCard(songViewModel: SongViewModel, song: Song) {
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
                songViewModel.addToQueue(song)
            }) {
                Text("Queue")
            }
        }
    }
}
