package edu.temple.beatbuddy

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import edu.temple.beatbuddy.ui.theme.BeatBuddyTheme
import kotlin.math.abs
import kotlin.math.roundToInt

class MainActivity : ComponentActivity(), SensorEventListener {
    private var cardViewModel = CardViewModel()
    private lateinit var sensorManager: SensorManager
    private var accelerometerSensor: Sensor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (accelerometerSensor == null) {
            Log.e("MainActivity", "No accelerometer sensor found")
        } else {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI)
        }

        setContent {
            BeatBuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SwipeableCardDeck(cardViewModel)
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val axisX = it.values[0]
                if (axisX > 2.0f) {
                    // Functionality for when device is tilted to the left
                    Log.d("MainActivity", "Tilted to the left")
                    cardViewModel.moveToNextCard("left")
                } else if (axisX < -2.0f) {
                    // Functionality for when device is tilted to the right
                    Log.d("MainActivity", "Tilted to the right")
                    cardViewModel.moveToNextCard("right")
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
}

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

@Composable
fun SwipeableCard(onDismiss: (String) -> Unit, content: @Composable () -> Unit) {
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

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    BeatBuddyTheme {
//        SwipeCard()
//    }
//}