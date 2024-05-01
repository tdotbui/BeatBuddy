package edu.temple.beatbuddy.music_swipe.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import edu.temple.beatbuddy.music_swipe.view_model.SwipeSongViewModel

abstract class SensorHandler(
    private val context: Context,
) : SensorEventListener, MeasurableSensor() {
    private var sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var isNeutral = true


    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = event.values[0]

                if (x > 5.0f && isNeutral) {
                    onSensorValuesChanged?.invoke(Direction.LEFT)
                    Log.d("SensorHandler", "Tilted left")
                    isNeutral = false
                } else if (x < -5.0f && isNeutral) {
                    onSensorValuesChanged?.invoke(Direction.RIGHT)
                    Log.d("SensorHandler", "Tilted right")
                    isNeutral = false
                } else if (x > -1.0f && x < 1.0f) {
                    isNeutral = true
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun register() {
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        }
    }

    private fun unregister() {
        sensorManager.unregisterListener(this)
    }

    override fun startListening() {
        Log.d("SensorHandler", "Start listening now...")
        register()
    }

    override fun stopListening() {
        Log.d("SensorHandler", "Stop listening now...")
        unregister()
    }
}