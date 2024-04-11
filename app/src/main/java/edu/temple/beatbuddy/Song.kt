package edu.temple.beatbuddy

import android.net.Uri
import java.util.UUID

data class Song(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val artist: String,
    val media: String
)

//val songs = listOf<Song>(
//    Song(
//        title = "Incredible (feat. Labrinth)",
//        artist = "Sia",
//        media = "https://cdns-preview-7.dzcdn.net/stream/c-792912511f37c14d55918e5f6db232a7-1.mp3"
//    ),
//
//)
