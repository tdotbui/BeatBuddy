package edu.temple.beatbuddy.browse_music.repository

import edu.temple.beatbuddy.browse_music.model.local.Song
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SongListRepository {
    suspend fun getSongList(
        fetchFromRemote: Boolean,
        genre: Int,
    ): Flow<Resource<List<Song>>>

    suspend fun getSongById(id: Long): Flow<Resource<Song>>
}