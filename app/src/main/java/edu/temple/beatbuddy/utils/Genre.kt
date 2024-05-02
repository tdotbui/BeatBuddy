package edu.temple.beatbuddy.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.ui.graphics.vector.ImageVector

enum class Genre(
    val id: Int,
    val title: String,
    val image: ImageVector
) {
    POP(31061, "Pop", Icons.Default.Album),
    HIPHOP(30991, "Hip Hop", Icons.Default.Album),
    OPERA(37061, "Opera", Icons.Default.Album),
    POP_ROCK(30931, "Pop-Rock", Icons.Default.Album),
    DANCE(42122, "Dance", Icons.Default.Album),
    JAZZ(42482, "Jazz", Icons.Default.Album),
    ELECTRONIC(42102, "Electronic", Icons.Default.Album),
    LATIN(36791, "Latin", Icons.Default.Album),
}
