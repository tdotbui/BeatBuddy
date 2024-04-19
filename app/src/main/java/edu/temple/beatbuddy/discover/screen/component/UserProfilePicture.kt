package edu.temple.beatbuddy.discover.screen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import edu.temple.beatbuddy.app.screen.HomeScreen
import edu.temple.beatbuddy.utils.ImageSize

@Composable
fun ProfilePicture(
    userProfile: String,
    size: Dp
) {
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(userProfile)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = 1.dp,
            color = Color.Green
        ),
        modifier = Modifier
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        if (imageState.painter != null) {
            Image(
                painter = imageState.painter!!,
                contentDescription = "User profile picture",
                modifier = Modifier.size(size),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "User profile picture",
                modifier = Modifier.size(size),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileImagePV() {
    ProfilePicture(
        userProfile = "",
        size = ImageSize.medium
    )
}