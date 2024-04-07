package edu.temple.beatbuddy.music.model.remote

import edu.temple.beatbuddy.music.model.remote.response.SongDto
import kotlinx.coroutines.flow.Flow

interface TopSongsRepository {
    suspend fun getTopSongs(
        fetchFromRemote: Boolean = true
    ): Flow<Resource<List<SongDto>>>

    suspend fun getSong(url: String): Flow<Resource<SongDto>>
}