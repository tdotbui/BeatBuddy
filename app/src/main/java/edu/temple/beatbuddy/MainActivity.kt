package edu.temple.beatbuddy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import edu.temple.beatbuddy.ui.theme.BeatBuddyTheme
import kotlin.math.abs
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeatBuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SwipeableCardDeck(cardContents = getDummyCardData())
                }
            }
        }
    }
}

@Composable
fun SwipeableCardDeck(cardContents: List<CardInfo>) {
    var currentIndex by remember {
        mutableIntStateOf(0)
    }
    val maxIndex = cardContents.size - 1

    fun moveToNextCard(dismissDirection: String) {
        when (dismissDirection) {
            "right" -> if (currentIndex < maxIndex) currentIndex++
            "left" -> if (currentIndex > 0) currentIndex--
        }
    }

    SwipeCard(
        onDismiss = { direction -> moveToNextCard(direction) }
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
            Text(text = "Title: ${cardContents[currentIndex].title}", style = MaterialTheme.typography.titleLarge)
            Text(text = "Artist: ${cardContents[currentIndex].artist}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun SwipeCard(onDismiss: (String) -> Unit, content: @Composable () -> Unit) {
    var offsetX by remember {
        mutableFloatStateOf(0f)
    }
    var offsetY by remember {
        mutableFloatStateOf(0f)
    }

    val swipeThreshold: Float = 400f
    val sensitivityFactor: Float = 3f

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(16.dp)
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        when {
                            offsetX > swipeThreshold -> onDismiss("right")
                            offsetX < -swipeThreshold -> onDismiss("left")
                            offsetY > swipeThreshold -> onDismiss("down")
                            offsetY < -swipeThreshold -> onDismiss("up")
                        }
                        offsetX = 0f
                        offsetY = 0f
                    }
                ) { change, dragAmount ->
                    offsetX += (dragAmount.x / density) * sensitivityFactor
                    offsetY += (dragAmount.y / density) * sensitivityFactor

                    if (change.positionChange() != Offset.Zero) {
                        change.consume()
                    }
                }
            }
            .graphicsLayer {
                alpha = 1 - (0.5f * (maxOf(abs(offsetX), abs(offsetY)) / swipeThreshold))
                rotationZ = (offsetX / swipeThreshold) * 30
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }
    }
}

data class CardInfo(val title: String, val artist: String)

fun getDummyCardData(): List<CardInfo> {
    return listOf(
        CardInfo("Song One", "Artist A"),
        CardInfo("Song Two", "Artist B"),
        CardInfo("Song Three", "Artist C"),
        CardInfo("Song Four", "Artist D"),
        CardInfo("Song Five", "Artist E")
    )
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    BeatBuddyTheme {
//        SwipeCard()
//    }
//}