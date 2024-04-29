package edu.temple.beatbuddy.music_playlist.repository

import android.util.Log
import edu.temple.beatbuddy.music_playlist.model.Playlist
import edu.temple.beatbuddy.music_playlist.model.PlaylistSong
import edu.temple.beatbuddy.music_playlist.model.PlaylistSongCrossRef
import edu.temple.beatbuddy.music_playlist.model.local.PlaylistDatabase
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val db: PlaylistDatabase
): PlaylistRepository {

    private val favoritePlaylist = Playlist(name = "Favorite", imageUrl = "https://cdn4.iconfinder.com/data/icons/ui-for-multimedia-players/48/21_favorite_music_favorite_list_note_love_favorite_heart_like_music_musical_notes_song_audio_tone_dot-1024.png")
    override suspend fun insertSongToPlayList(playlist: Playlist, song: PlaylistSong) = try {
        val songId = db.playlistSongDao.insertSong(song)
        Log.d("Result", "Song Id is $songId")
        val existingPlaylist = db.playlistDao.getPlaylistById(playlist.id)
        Log.d("Result", "playlist is $existingPlaylist")

        if (existingPlaylist == null) {
            val newPlaylistId = db.playlistDao.insertPlaylist(playlist)
            val crossRef = PlaylistSongCrossRef(newPlaylistId, songId)
            db.playlistSongCrossRef.insertPlaylistSongCrossRef(crossRef)
        } else {
            Log.d("Result", "Playlist existed ${playlist.id}")
            val crossRef = PlaylistSongCrossRef(playlist.id, songId)
            db.playlistSongCrossRef.insertPlaylistSongCrossRef(crossRef)
        }
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!)
    }

    override suspend fun getAllPlaylists(): Resource<List<Playlist>> = try {
            createFavoritePlaylist()
            val playlists = db.playlistDao.getAllPlaylists()
            Log.d("Result", "Size of playlist ${playlists.size}")
            Resource.Success(playlists)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage!!)
        }

    override suspend fun getAllSongsFromPlayList(playlist: Playlist): Flow<Resource<List<PlaylistSong>>> = flow {
        emit(Resource.Loading(true))
        try {
            Log.d("Result", "Songlist is run in this function")
            val songs = db.playlistSongDao.getSongsForPlaylist(playlist.id)
            Log.d("Result", "Songlist is ${songs.first()}")
            emit(Resource.Success(songs))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage!!))
        } finally {
            emit(Resource.Loading(false))
        }
    }

    private suspend fun createFavoritePlaylist() {
        val existingFavorite = db.playlistDao.getPlaylistByName(favoritePlaylist.name)
        if (existingFavorite == null) {
            db.playlistDao.insertPlaylist(favoritePlaylist)
        }
    }
}