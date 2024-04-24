package edu.temple.beatbuddy.music_player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MusicPlayerSmallScreen(
    expand: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(Color.Cyan)
            .clickable {
                expand()
            }
    ) {

    }
}