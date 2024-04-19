package edu.temple.beatbuddy.browse_music.model.remote

data class SongDto(
    val id: Long?,

    val album: AlbumDto?,
    val artist: ArtistDto?,
    val duration: Int?,
    val explicit_content_cover: Int?,
    val explicit_content_lyrics: Int?,
    val explicit_lyrics: Boolean?,
    val link: String?,
    val md5_image: String?,
    val preview: String?,
    val rank: Int?,
    val readable: Boolean?,
    val title: String?,
    val title_short: String?,
    val title_version: String?,
    val type: String?
)