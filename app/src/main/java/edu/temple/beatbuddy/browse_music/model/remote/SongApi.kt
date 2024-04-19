package edu.temple.beatbuddy.browse_music.model.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface SongApi {

    @GET("radio/{genre}/tracks")
    suspend fun getSongList(
        @Path("genre") genre: Int
    ): SongListDto

    companion object {
        const val BASE_URL = "https://api.deezer.com/"
    }
}