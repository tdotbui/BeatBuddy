package edu.temple.beatbuddy.music_playlist.repository

import edu.temple.beatbuddy.music_playlist.model.Playlist
import edu.temple.beatbuddy.music_playlist.model.PlaylistSong
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun insertSongToPlayList(playlist: Playlist, song: PlaylistSong): Resource<Boolean>

    suspend fun getAllPlaylists(): Resource<List<Playlist>>
    suspend fun getAllSongsFromPlayList(playlist: Playlist): Flow<Resource<List<PlaylistSong>>>
}