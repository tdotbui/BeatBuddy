package edu.temple.beatbuddy.music.model.remote.response

data class SongDto(
    val artist: Artist,
    val duration: String,
    val image: List<Image>,
    val listeners: String,
    val mbid: String,
    val name: String,
    val playcount: String,
    val streamable: Streamable,
    val url: String
)