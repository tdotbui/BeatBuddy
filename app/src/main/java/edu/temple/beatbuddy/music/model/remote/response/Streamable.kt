package edu.temple.beatbuddy.music.model.remote.response

import com.google.gson.annotations.SerializedName

data class Streamable(
    @SerializedName("#text") val text: String,
    val fulltrack: String
)