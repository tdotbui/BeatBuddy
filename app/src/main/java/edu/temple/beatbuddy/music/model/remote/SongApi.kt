package edu.temple.beatbuddy.music.model.remote

import edu.temple.beatbuddy.music.model.remote.response.SongListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SongApi {
    @GET("")
    suspend fun getTopTracks(
        @Query("method") method: String,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("format") format: String = "json"
    ): SongListDto

    companion object {
        const val BASE_URL = "https://ws.audioscrobbler.com/2.0/"
        const val API_KEY = "3144ce31d43eb3401cab700dc917736b"
    }
}