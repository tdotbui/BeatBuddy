package edu.temple.beatbuddy.music_browse.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.outlined.ImageSearch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.temple.beatbuddy.component.ImageFactory
import edu.temple.beatbuddy.music_playlist.model.Playlist
import edu.temple.beatbuddy.utils.Genre

@Composable
fun PlaylistItem(
    playlist: Playlist,
    onClick: () -> Unit,
    isSelected: Boolean,
    onLongPress: (Playlist) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(100.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onLongPress(playlist) },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        ImageFactory(
            context = LocalContext.current,
            imageUrl = playlist.imageUrl,
            imageVector = Icons.Outlined.ImageSearch,
            description = "Playlist cover",
            modifier = Modifier
                .size(50.dp)
                .padding(8.dp)
                .alpha(if (isSelected) 1f else 0.7f),
        )

        Text(
            text = playlist.name,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .alpha(if (isSelected) 1f else 0.7f)
                .padding(bottom = 8.dp),
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Light,
            fontSize = 12.sp
        )
    }
}