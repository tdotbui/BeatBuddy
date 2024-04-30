package edu.temple.beatbuddy.music_browse.model.mapping

import edu.temple.beatbuddy.music_browse.model.Song
import edu.temple.beatbuddy.music_browse.model.remote.AlbumDto
import edu.temple.beatbuddy.music_browse.model.remote.ArtistDto
import edu.temple.beatbuddy.music_browse.model.remote.SongDto
import edu.temple.beatbuddy.music_playlist.model.PlaylistSong
import edu.temple.beatbuddy.music_post.model.SongPost

fun SongDto.toSong(
    genre: Int
): Song = Song(
    id = id ?: 0L,

    album = album ?: AlbumDto("", "", "", "", "", 0, "", "", "", ""),
    artist = artist ?: ArtistDto(0, "", "", "", "", "", "", "", "",""),
    duration = duration ?: 0,
    explicit_content_cover = explicit_content_cover ?: 0,
    explicit_content_lyrics = explicit_content_lyrics ?: 0,
    explicit_lyrics = explicit_lyrics ?: false,
    link = link ?: "",
    md5_image = md5_image ?: "",
    preview = preview ?: "",
    rank = rank ?: 0,
    readable = readable ?: false,
    title = title ?: "",
    title_short = title_short ?: "",
    title_version = title_version ?: "",
    type = type ?: "",

    genre = genre
)

fun Song.toPlaylistSong(): PlaylistSong = PlaylistSong(
    id = id,
    title = title,
    preview = preview,
    artistName = artist.name ?: "",
    artistPicture = artist.picture_medium ?: "",
    songImage = album.cover_medium ?: ""
)

fun SongPost.toPlaylistSong(): PlaylistSong = PlaylistSong(
    id = songId,
    title = title,
    preview = preview,
    artistName = artistName,
    artistPicture = artistPicture,
    songImage = songImage
)