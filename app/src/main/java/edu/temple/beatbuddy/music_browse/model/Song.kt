package edu.temple.beatbuddy.music_browse.model

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

data class MockSong (
    val songId: Long,
    val preview: String,
    val title: String,
    val artist: String,
    val songImage: String
)
object MockSongList {
    val songs = listOf(
        MockSong(
            songId = 2444176345,
            title = "greedy",
            preview = "https://cdns-preview-d.dzcdn.net/stream/c-d3798b1f541af7a8886af7e8fec035b9-2.mp3",
            artist = "Tate McRae",
            songImage = "https://e-cdns-images.dzcdn.net/images/cover/ef25b6bec265332a059879f45d33cd7e/250x250-000000-80-0-0.jpg",
        ),
        MockSong(
            songId = 463935645,
            title = "What Was I Made For? [From The Motion Picture \\\"Barbie\\\"]",
            preview = "https://cdns-preview-8.dzcdn.net/stream/c-840d9d9b9875e413cf5c00a5d6918626-6.mp3",
            artist = "Billie Eilish",
            songImage = "https://e-cdns-images.dzcdn.net/images/cover/2562b8d68b75635bb2d4b92dc7ed9ab5/250x250-000000-80-0-0.jpg",
        ),
    )
}