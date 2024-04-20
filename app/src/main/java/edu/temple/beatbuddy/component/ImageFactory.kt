package edu.temple.beatbuddy.component

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size

@Composable
fun ImageFactory(
    context: Context,
    imageUrl: String,
    imageVector: ImageVector,
    description: String,
    modifier: Modifier = Modifier
) {
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build()
    ).state

    if (imageState.painter != null) {
        Image(
            painter = imageState.painter!!,
            contentDescription = description,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    } else {
        Icon(
            modifier = Modifier.size(50.dp),
            imageVector = imageVector,
            contentDescription = "$description image",
            tint = Color.Gray
        )
    }
}