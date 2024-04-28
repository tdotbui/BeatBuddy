package edu.temple.beatbuddy.music_browse.repository

import android.util.Log
import edu.temple.beatbuddy.music_browse.model.local.Song
import edu.temple.beatbuddy.music_browse.model.local.SongDatabase
import edu.temple.beatbuddy.music_browse.model.mapping.toSong
import edu.temple.beatbuddy.music_browse.model.remote.SongApi
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SongListRepositoryImpl @Inject constructor(
    private val songApi: SongApi,
    private val songDatabase: SongDatabase
): SongListRepository {
    override suspend fun getSongList(
        fetchFromRemote: Boolean,
        genre: Int
    ): Flow<Resource<List<Song>>> = flow {
        emit(Resource.Loading(true))
        val localSongList = songDatabase.songDao.getSongListByGenre(genre)
        val shouldCache = localSongList.isNotEmpty() && !fetchFromRemote

        if (shouldCache) {
            emit(Resource.Success(localSongList))
            emit(Resource.Loading(false))
            Log.d("Result", "Fetch locally")
            return@flow
        }

        val songListFromRemote = try {
            Log.d("Result", "Fetch remotely")
            songApi.getSongList(genre)
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error(message = e.localizedMessage!!))
            return@flow
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Resource.Error(message = e.localizedMessage!!))
            return@flow
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(message = e.localizedMessage!!))
            return@flow
        }

        val songs = songListFromRemote.data.let {
            it.map { songDto ->
                songDto.toSong(genre)
            }
        }

        songDatabase.songDao.upsertSongList(songs)
        emit(Resource.Success(
            songDatabase.songDao.getSongListByGenre(genre)
        ))

        emit(Resource.Loading(false))
    }


    override suspend fun getSongById(
        id: Long
    ): Flow<Resource<Song>> = flow {
        emit(Resource.Loading(true))
        val localSong = songDatabase.songDao.getSongById(id)

        if (localSong != null) {
            emit(Resource.Success(localSong))
            emit(Resource.Loading(false))
            return@flow
        }

        emit(Resource.Error(message = "No selected song"))
        emit(Resource.Loading(false))
    }
}