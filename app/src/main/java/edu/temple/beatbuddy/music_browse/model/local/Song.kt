package edu.temple.beatbuddy.music_browse.model.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.temple.beatbuddy.music_browse.model.remote.AlbumDto
import edu.temple.beatbuddy.music_browse.model.remote.ArtistDto

@Entity("SongEntity")
data class Song(
    @PrimaryKey
    val id: Long,

    @Embedded val album: AlbumDto,
    @Embedded val artist: ArtistDto,

    val duration: Int,
    val explicit_content_cover: Int,
    val explicit_content_lyrics: Int,
    val explicit_lyrics: Boolean,
    val link: String,
    val md5_image: String,
    val preview: String,
    val rank: Int,
    val readable: Boolean,
    val title: String,
    val title_short: String,
    val title_version: String,
    val type: String,

    val genre: Int
)