package edu.temple.beatbuddy.music.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SongEntity(
    @PrimaryKey
    val id: String,

    val album: String,
    val artist: String,
    val buy_link: String,
    val description: String,
    val featuring: String,
    val genre: String,
    val image: String,
    val live: Boolean,
    val producer: String,
    val released: String,
    val status: String,
    val stream_only: String,
    val streaming_url: String,
    val streaming_url_timeout: Int,
    val time_ago: String,
    val title: String,
    val type: String,
    val updated: String,
    val uploaded: String,
    val uploader: Uploader,
    val url_slug: String
)