package edu.temple.beatbuddy

import java.util.UUID

data class Song(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val artist: String,
    val media: Int
)
