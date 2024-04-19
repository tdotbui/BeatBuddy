package edu.temple.beatbuddy.music_browse.model.remote

import com.google.gson.annotations.SerializedName

data class ArtistDto(
    @SerializedName("id") val artistId: Int?,
    @SerializedName("link") val artistLink: String?,
    val name: String?,
    val picture: String?,
    val picture_big: String?,
    val picture_medium: String?,
    val picture_small: String?,
    val picture_xl: String?,
    val tracklist: String?,
    @SerializedName("type") val artistType: String?
)