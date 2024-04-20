package edu.temple.beatbuddy.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import edu.temple.beatbuddy.R
import edu.temple.beatbuddy.app.screen.HomeScreen
import edu.temple.beatbuddy.music_post.model.MockPost

@Composable
fun VinylAlbumCover(
    modifier: Modifier = Modifier,
    rotationDegrees: Float = 0f,
    imageUrL: String
) {
    val context = LocalContext.current

    val roundedShape = object : Shape {
        override fun createOutline(
            size: Size,
            layoutDirection: LayoutDirection,
            density: Density
        ): Outline {
            val p1 = Path().apply {
                addOval(Rect(4f, 3f, size.width - 1, size.height - 1))
            }
            val thickness = size.height / 2.10f
            val p2 = Path().apply {
                addOval(
                    Rect(
                        thickness,
                        thickness,
                        size.width - thickness,
                        size.height - thickness
                    )
                )
            }
            val p3 = Path()
            p3.op(p1, p2, PathOperation.Difference)

            return Outline.Generic(p3)
        }
    }

    Box(
        modifier = modifier
            .aspectRatio(1.0f)
            .clip(roundedShape)
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .rotate(rotationDegrees),
            painter = painterResource(id = R.drawable.vinyl_background),
            contentDescription = "vinyl background"
        )

        ImageFactory(
            context = context,
            imageUrl = imageUrL,
            imageVector = Icons.Default.MusicNote,
            description = "song album cover",
            modifier = Modifier
                .fillMaxSize(0.5f)
                .rotate(rotationDegrees)
                .aspectRatio(1.0f)
                .align(Alignment.Center)
                .clip(roundedShape),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VinylCoverPV() {
    VinylAlbumCover(
        imageUrL = MockPost.posts[0].songImage
    )
}