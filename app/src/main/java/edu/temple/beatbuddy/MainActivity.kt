package edu.temple.beatbuddy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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
                    SwipeCard { CardInfo() }
                }
            }
        }
    }
}

@Composable
fun CardInfo() {
    Text("Title")
    Text("Artist")
}

@Composable
fun SwipeCard(content: @Composable () -> Unit) {
    var offsetX by remember {
        mutableFloatStateOf(0f)
    }
    var offsetY by remember {
        mutableFloatStateOf(0f)
    }
    var dismissRight by remember {
        mutableStateOf(false)
    }
    var dismissLeft by remember {
        mutableStateOf(false)
    }
    var dismissUp by remember {
        mutableStateOf(false)
    }
    var dismissDown by remember {
        mutableStateOf(false)
    }

    val swipeThreshold: Float = 400f
    val sensitivityFactor: Float = 3f

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(48.dp)
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        offsetX = 0f
                        offsetY = 0f
                    }
                ) { change, dragAmount ->
                    val dragAmountX = (dragAmount.x / density) * sensitivityFactor
                    val dragAmountY = (dragAmount.y / density) * sensitivityFactor
                    offsetX += dragAmountX
                    offsetY += dragAmountY
                    when {
                        offsetX > swipeThreshold -> {
                            Log.d("SwipeCard", "RIGHT SWIPE DETECTED")
                            dismissRight = true
                        }

                        offsetX < -swipeThreshold -> {
                            Log.d("SwipeCard", "LEFT SWIPE DETECTED")
                            dismissLeft = true
                        }

                        offsetY > swipeThreshold -> {
                            Log.d("SwipeCard", "DOWN SWIPE DETECTED")
                            dismissDown = true
                        }

                        offsetY < -swipeThreshold -> {
                            Log.d("SwipeCard", "UP SWIPE DETECTED")
                            dismissUp = true
                        }
                    }

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
        content()
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    BeatBuddyTheme {
//        SwipeCard()
//    }
//}