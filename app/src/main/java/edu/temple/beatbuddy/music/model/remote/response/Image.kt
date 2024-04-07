package edu.temple.beatbuddy.music.model.remote.response

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("#text") val text: String,
    val size: String
)