package edu.temple.beatbuddy.music_player.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward5
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay5
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import edu.temple.beatbuddy.component.VinylAlbumCover
import edu.temple.beatbuddy.component.VinylAlbumCoverAnimation
import edu.temple.beatbuddy.music_player.player.PlayerEvent
import edu.temple.beatbuddy.music_player.view_model.SongViewModel
import edu.temple.beatbuddy.utils.toTime

@Composable
fun MusicPlayerScreen(
    songViewModel: SongViewModel,
    playerEvent: PlayerEvent,
) {
    val isFullScreen by songViewModel.isFullScreen.collectAsState()

    if (isFullScreen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.TopCenter
        ) {
            FullPlayerContent(
                songViewModel = songViewModel,
                playToggle = { playerEvent.onPlayPauseClick() },
                rewind = { playerEvent.onRewindClick() },
                forward = { playerEvent.onForwardClick() },
                playBack = { playerEvent.onPreviousClick() },
                playNext = { playerEvent.onNextClick() },
                sliderChanged = { position-> playerEvent.onSeekBarPositionChanged(position.toLong()) },
                slideDown = {
                    songViewModel.minimizeScreen()
                }
            )
        }
    } else {
        BriefPlayerContent(
            songViewModel = songViewModel,
            playToggle = { playerEvent.onPlayPauseClick() },
            playNext = { playerEvent.onNextClick() },
            slideUp = {
                songViewModel.maximizeScreen()
            },
        )
    }
}

@Composable
fun BriefPlayerContent(
    songViewModel: SongViewModel,
    playToggle: () -> Unit,
    playNext: () -> Unit,
    slideUp: () -> Unit,
) {
    val isPlaying by songViewModel.isPlaying.collectAsState()
    val currentSong by songViewModel.selectedSong.collectAsState()

    LaunchedEffect(currentSong, isPlaying) {
        Log.d("", "something changed")
    }

    Box(
        modifier = Modifier
            .height(64.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.LightGray, Color.Gray, Color.DarkGray),
                    endY = LocalConfiguration.current.screenHeightDp.toFloat() * LocalDensity.current.density
                )
            )
            .clickable { slideUp() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (isPlaying) {
                VinylAlbumCoverAnimation(
                    imageUrl = currentSong?.album?.cover_medium ?: "",
                    modifier = Modifier
                        .padding(4.dp)
                        .padding(start = 12.dp)
                )
            } else {
                VinylAlbumCover(
                    imageUrL = currentSong?.album?.cover_medium ?: "",
                    modifier = Modifier
                        .padding(4.dp)
                        .padding(start = 12.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 8.dp, horizontal = 32.dp),
            ) {
                Text(
                    text = currentSong?.title ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = currentSong?.artist?.name ?: "artist name",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .alpha(0.5f)
                )
            }

            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "pause" else "play",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(48.dp)
                    .clickable { playToggle() }
            )

            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "play next song",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { playNext() }
                    .padding(end = 12.dp)
                    .size(32.dp)
            )
        }
    }
}

@Composable
fun FullPlayerContent(
    songViewModel: SongViewModel,
    playToggle: () -> Unit,
    rewind: () -> Unit,
    forward: () -> Unit,
    playBack: () -> Unit,
    playNext: () -> Unit,
    sliderChanged: (Float) -> Unit,
    slideDown: () -> Unit
) {
    val isPlaying by songViewModel.isPlaying.collectAsState()
    val currentSong by songViewModel.selectedSong.collectAsState()
    val currentTime = songViewModel.player.currentPlaybackPosition.toFloat()

    LaunchedEffect(currentSong, isPlaying) {
        Log.d("", "something changed")
    }

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
                            onClick = slideDown
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
                            if (isPlaying) {
                                VinylAlbumCoverAnimation(imageUrl = currentSong?.album?.cover_medium ?: "")
                            } else {
                                VinylAlbumCover(imageUrL = currentSong?.album?.cover_medium ?: "")
                            }
                        }

                        Text(
                            text = currentSong?.title ?: "",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = currentSong?.artist?.name ?: "",
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
                                        text = currentTime.toLong().toTime(),
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
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "pause" else "play",
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

