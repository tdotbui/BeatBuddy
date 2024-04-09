package edu.temple.beatbuddy.music.model.mapping

import edu.temple.beatbuddy.music.model.local.Song
import edu.temple.beatbuddy.music.model.remote.SongDto

fun SongDto.toSong(
    genre: Int
): Song = Song(
    id = id,

    album = album,
    artist = artist,
    duration = duration,
    explicit_content_cover = explicit_content_cover,
    explicit_content_lyrics = explicit_content_lyrics,
    explicit_lyrics = explicit_lyrics,
    link = link,
    md5_image = md5_image,
    preview = preview,
    rank = rank,
    readable = readable,
    title = title,
    title_short = title_short,
    title_version = title_version,
    type = type,

    genre = genre
)