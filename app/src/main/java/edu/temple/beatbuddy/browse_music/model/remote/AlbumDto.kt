package edu.temple.beatbuddy.browse_music.model.remote

import com.google.gson.annotations.SerializedName

data class AlbumDto(
    val cover: String?,
    val cover_big: String?,
    val cover_medium: String?,
    val cover_small: String?,
    val cover_xl: String?,
    @SerializedName("id") val albumId: Int?,
    @SerializedName("md5_image") val albumImage: String?,
    @SerializedName("title") val albumTitle: String?,
    @SerializedName("tracklist") val albumTrackList: String?,
    @SerializedName("type") val albumType: String?
)