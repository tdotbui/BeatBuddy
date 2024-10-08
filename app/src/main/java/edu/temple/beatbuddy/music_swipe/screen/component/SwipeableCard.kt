package edu.temple.beatbuddy.music_swipe.screen.component

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.temple.beatbuddy.music_swipe.sensor.Direction
import edu.temple.beatbuddy.music_swipe.view_model.SensorViewModel
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun SwipeableCard(
    sensorViewModel: SensorViewModel,
    onDismiss: (Direction) -> Unit,
    content: @Composable () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    val swipeThreshold: Float = 400f
    val sensitivityFactor: Float = 3f

    val isSensorActive by sensorViewModel.isSensorActive.collectAsState()
    val direction by sensorViewModel.direction.collectAsState()

    when (direction) {
        Direction.LEFT -> {
            if (isSensorActive) {
                onDismiss(Direction.LEFT)
                sensorViewModel.resetDirection()
            }
        }
        Direction.RIGHT -> {
            if (isSensorActive) {
                onDismiss(Direction.RIGHT)
                sensorViewModel.resetDirection()
            }
        }
        else -> {}
    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        // Determine swipe direction and call onDismiss if the swipe exceeds threshold.
                        when {
                            offsetX > swipeThreshold -> onDismiss(Direction.RIGHT)
                            offsetX < -swipeThreshold -> onDismiss(Direction.LEFT)
//                            offsetY > swipeThreshold -> onDismiss("down")
//                            offsetY < -swipeThreshold -> onDismiss("up")
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
                // Gradually change the card's alpha and rotation based on offset to give visual feedback.
                alpha = 1 - (0.5f * (maxOf(abs(offsetX), abs(offsetY)) / swipeThreshold))
                rotationZ = (offsetX / swipeThreshold) * 30
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.wrapContentSize()
        ) {
            content()
        }
    }
}