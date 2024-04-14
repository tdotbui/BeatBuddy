package edu.temple.beatbuddy.music.model.mapping

import edu.temple.beatbuddy.music.model.local.Song
import edu.temple.beatbuddy.music.model.remote.SongDto

fun SongDto.toSong(
    genre: Int
): Song = Song(
    id = id ?: 0L,

    album = album,
    artist = artist,
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