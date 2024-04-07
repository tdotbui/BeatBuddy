package edu.temple.beatbuddy.music.model.remote.response

import com.google.gson.annotations.SerializedName

data class Tracks(
    @SerializedName("@attr") val attr: Attr,
    val track: List<SongDto>
)