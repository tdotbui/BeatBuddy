package edu.temple.beatbuddy.music.model

import androidx.room.Entity
import edu.temple.beatbuddy.music.model.remote.response.Artist
import edu.temple.beatbuddy.music.model.remote.response.Image
import edu.temple.beatbuddy.music.model.remote.response.Streamable

@Entity
data class Song(
    val artist: Artist,
    val duration: String,
    val image: List<Image>,
    val listeners: String,
    val mbid: String,
    val name: String,
    val playcount: String,
    val streamable: Streamable,
    val url: String,

    val id: Long
)
