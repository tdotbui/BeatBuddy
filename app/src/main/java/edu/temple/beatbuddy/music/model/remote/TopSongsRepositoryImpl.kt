package edu.temple.beatbuddy.music.model.remote

import edu.temple.beatbuddy.music.model.remote.response.SongDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TopSongsRepositoryImpl @Inject constructor(
    private val songApi: SongApi
): TopSongsRepository {
    override suspend fun getTopSongs(fetchFromRemote: Boolean): Flow<Resource<List<SongDto>>> {
        return flow {
            emit(Resource.Loading(true))
        }
    }

    override suspend fun getSong(url: String): Flow<Resource<SongDto>> {
        TODO("Not yet implemented")
    }
}