package edu.temple.beatbuddy.music_swipe.sensor

import android.content.Context

abstract class MeasurableSensor {
    protected var onSensorValuesChanged: ((Direction) -> Unit)? = null

    abstract fun startListening()
    abstract fun stopListening()

    fun setOnSensorValuesChangedListener(listener: (Direction) -> Unit) {
        onSensorValuesChanged = listener
    }
}

class AccelerometerSensor(
    context: Context
): SensorHandler(context)

enum class Direction {
    RIGHT, LEFT, NONE
}