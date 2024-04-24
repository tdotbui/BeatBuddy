package edu.temple.beatbuddy.music_player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward5
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Replay5
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.temple.beatbuddy.component.VinylAlbumCoverAnimation
import edu.temple.beatbuddy.music_browse.model.local.MockSong
import edu.temple.beatbuddy.music_browse.model.local.MockSongList
import edu.temple.beatbuddy.music_browse.model.local.Song
import edu.temple.beatbuddy.utils.toTime

@Composable
fun MusicPlayerFullScreen(
    song: Song,
    close: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        PlayerContent(
            song = song,
            currentTime = 2L,
            isPlaying = true,
            playToggle = {},
            rewind = {},
            forward = {},
            playBack = {},
            playNext = {},
            sliderChanged = {},
            close = close
        )
    }
}

@Composable
fun PlayerContent(
    song: Song,
    currentTime: Long,
    isPlaying: Boolean,
    playToggle: () -> Unit,
    rewind: () -> Unit,
    forward: () -> Unit,
    playBack: () -> Unit,
    playNext: () -> Unit,
    sliderChanged: (Float) -> Unit,
    close: () -> Unit
) {
    val sliderColor = SliderDefaults.colors(
        thumbColor = MaterialTheme.colorScheme.onBackground,
        activeTrackColor = MaterialTheme.colorScheme.onBackground,
        inactiveTrackColor = MaterialTheme.colorScheme.onBackground
    )

    Box(
        modifier =  Modifier
            .fillMaxSize()
    ) {
        Surface {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.LightGray, Color.Gray, Color.DarkGray),
                            endY = LocalConfiguration.current.screenHeightDp.toFloat() * LocalDensity.current.density
                        )
                    )
                    .fillMaxSize()
                    .padding(bottom = 32.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = close
                        ) {
                            Image(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "close",
                                colorFilter = ColorFilter.tint(LocalContentColor.current)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .weight(1f)
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            VinylAlbumCoverAnimation(imageUrl = song.album.cover_medium ?: "")
                        }

                        Text(
                            text = song.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = song.artist.name ?: "",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.graphicsLayer {
                                alpha = 0.60f
                            }
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            Slider(
                                value = currentTime.toFloat(),
                                modifier = Modifier.fillMaxWidth(),
                                valueRange = 0f..100f,
                                colors = sliderColor,
                                onValueChange = sliderChanged,
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CompositionLocalProvider {
                                    Text(
                                        text = currentTime.toTime(),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                CompositionLocalProvider {
                                    Text(
                                        30000L.toTime(), style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipPrevious,
                                contentDescription = "play previous song",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable { playBack() }
                                    .padding(12.dp)
                                    .size(32.dp)
                            )

                            Icon(
                                imageVector = Icons.Default.Replay5,
                                contentDescription = "rewind 5 seconds",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable { rewind() }
                                    .padding(12.dp)
                                    .size(32.dp)
                            )

                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "play",
                                tint = MaterialTheme.colorScheme.background,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.onBackground)
                                    .clickable { playToggle() }
                                    .size(64.dp)
                                    .padding(8.dp)
                            )

                            Icon(
                                imageVector = Icons.Default.Forward5,
                                contentDescription = "forward 5 seconds",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable { forward() }
                                    .padding(12.dp)
                                    .size(32.dp)
                            )

                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = "play next song",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable { playNext() }
                                    .padding(12.dp)
                                    .size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PlayerPV() {
//    MusicPlayerFullScreen(
//        song = MockSongList.songs[0],
//        close = {}
//    )
//}