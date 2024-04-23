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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import edu.temple.beatbuddy.ui.theme.BeatBuddyTheme

class MainActivity : ComponentActivity() {
    private var cardViewModel = CardViewModel()
    private lateinit var sensorHandler: SensorHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorHandler = SensorHandler(this)

        setContent {
            BeatBuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SwipeableCardDeck(cardViewModel)
                    Row {
                        Button(onClick = {
                            cardViewModel.moveToNextCard("left")
                        }) {
                            Text("Backward")
                        }
                        Button(onClick = {
                            cardViewModel.moveToNextCard("right")
                        }) {
                            Text("Forward")
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sensorHandler.unregister()
    }

    override fun onResume() {
        super.onResume()
        sensorHandler.register()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorHandler.unregister()
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    BeatBuddyTheme {
//        SwipeCard()
//    }
//}