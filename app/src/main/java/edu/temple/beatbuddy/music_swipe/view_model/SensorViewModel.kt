package edu.temple.beatbuddy.music_swipe.view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.music_swipe.sensor.Direction
import edu.temple.beatbuddy.music_swipe.sensor.MeasurableSensor
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SensorViewModel @Inject constructor(
    private val sensor: MeasurableSensor
): ViewModel() {

    var direction = MutableStateFlow(Direction.NONE)

    var isSensorActive = MutableStateFlow(false)

    init {
        sensor.setOnSensorValuesChangedListener { result ->
            direction.value = result
        }
    }

    fun resetDirection() {
        direction.value = Direction.NONE
    }

    fun startListening() {
        sensor.startListening()
        isSensorActive.value = true
//        Log.d("SensorHandler", "Start listening now...")
    }

    fun stopListening() {
        sensor.stopListening()
        isSensorActive.value = false
//        Log.d("SensorHandler", "Stop listening now...")
    }
}