package edu.temple.beatbuddy.music.repository

import edu.temple.beatbuddy.music.model.Song
import edu.temple.beatbuddy.music.model.mapping.toSong
import edu.temple.beatbuddy.music.model.remote.SongApi
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SongListRepositoryImpl @Inject constructor(
    private val songApi: SongApi
): SongListRepository {
    override suspend fun getSongList(
        fetchFromRemote: Boolean,
        genre: Int
    ): Flow<Resource<List<Song>>> {
        return flow {
            val songListFromRemote = try {
                songApi.getSongList(genre)
            } catch (e: IOException) {
                e.printStackTrace()
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                return@flow
            }

            val songs = songListFromRemote.data.let {
                it.map { songDto ->
                    songDto.toSong(genre)
                }
            }

            emit(Resource.Success(songs))
        }
    }

//    override suspend fun getSongById(id: Long): Flow<Resource<Song>> {
//        TODO("Not yet implemented")
//    }
}