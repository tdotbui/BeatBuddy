package edu.temple.beatbuddy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SwipeableCardDeck(cardViewModel: CardViewModel) {
    val currentIndex by cardViewModel.currentIndex.collectAsState()

    SwipeableCard(
        onDismiss = { direction -> cardViewModel.moveToNextCard(direction) }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "Title: ${cardViewModel.getDummyCardData()[currentIndex].title}", style = MaterialTheme.typography.titleLarge)
            Text(text = "Artist: ${cardViewModel.getDummyCardData()[currentIndex].artist}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}