package edu.temple.beatbuddy.music_swipe.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.music_swipe.sensor.Direction
import edu.temple.beatbuddy.music_swipe.sensor.MeasurableSensor
import javax.inject.Inject

@HiltViewModel
class SensorViewModel @Inject constructor(
    val sensor: MeasurableSensor
): ViewModel() {

    var direction by mutableStateOf(Direction.NONE)

    init {
        sensor.setOnSensorValuesChangedListener { result ->
            direction = result
        }
    }
}