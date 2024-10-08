package edu.temple.beatbuddy.music_browse.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.temple.beatbuddy.utils.Genre

@Composable
fun GenreItem(
    genre: Genre,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(100.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            imageVector = if (isSelected) Icons.Default.LibraryMusic else genre.image,
            contentDescription = genre.title,
            modifier = Modifier
                .size(50.dp)
                .padding(8.dp)
                .alpha(if (isSelected) 1f else 0.7f),
            contentScale = ContentScale.Crop,
        )

        Text(
            text = genre.title,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .alpha(if (isSelected) 1f else 0.7f)
                .padding(bottom = 8.dp),
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Light,
            fontSize = 12.sp
        )
    }
}