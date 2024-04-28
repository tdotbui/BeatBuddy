    package edu.temple.beatbuddy

    import android.content.Context
    import android.hardware.Sensor
    import android.hardware.SensorEvent
    import android.hardware.SensorEventListener
    import android.hardware.SensorManager
    import android.util.Log

    class SensorHandler(private val context: Context, private val cardViewModel: CardViewModel) : SensorEventListener {
        private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        private var accelerometerSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        private var isNeutral = true

        init {
            if (accelerometerSensor == null) {
                 Log.d("SensorHandler", "No accelerometer sensor found")
            } else {
                Log.d("SensorHandler", "Accelerometer sensor found")
                register()
            }
        }

        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    val xAxis = event.values[0]
//                    Log.d("SensorHandler", "X-axis: $xAxis")

                    if (xAxis > 5.0f && isNeutral) {
                        Log.d("SensorHandler", "Tilted left")
                        cardViewModel.moveToNextCard("left")
                        isNeutral = false
                    } else if (xAxis < -5.0f && isNeutral) {
                        Log.d("SensorHandler", "Tilted right")
                        cardViewModel.moveToNextCard("right")
                        isNeutral = false
                    } else if (xAxis > -0.5f && xAxis < 0.5f) {
                        isNeutral = true
                    }
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        fun register() {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI)
        }

        fun unregister() {
            sensorManager.unregisterListener(this)
        }
    }