package edu.temple.beatbuddy

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class SensorHandler(private val context: Context) : SensorEventListener {
    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelerometerSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var cardViewModel = CardViewModel()

    init {
        if (accelerometerSensor == null) {
             Log.d("SensorHandler", "No accelerometer sensor found")
        } else {
            Log.d("SensorHandler", "Accelerometer sensor found")
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val xAxis = event.values[0]

                if (xAxis > 1.0f) {
                    Log.d("SensorHandler", "Tilted left")
                    cardViewModel.moveToNextCard("left")
                } else if (xAxis < -1.0f) {
                    Log.d("SensorHandler", "Tilted right")
                    cardViewModel.moveToNextCard("right")
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun unregister() {
        sensorManager.unregisterListener(this)
    }
}