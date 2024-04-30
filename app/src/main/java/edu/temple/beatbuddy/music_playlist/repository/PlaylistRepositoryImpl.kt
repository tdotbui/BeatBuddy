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
        val existingSong = db.playlistSongDao.getSongsForPlaylist(playlist.id).find {
            it.id == song.id
        }
        if (existingSong != null) {
            Resource.Success(false)
        } else {
            val songId = db.playlistSongDao.insertSong(song)
            val existingPlaylist = db.playlistDao.getPlaylistById(playlist.id)

            if (existingPlaylist == null) {
                val newPlaylistId = db.playlistDao.insertPlaylist(playlist)
                val crossRef = PlaylistSongCrossRef(newPlaylistId, songId)
                db.playlistSongCrossRef.insertPlaylistSongCrossRef(crossRef)
            } else {
                val crossRef = PlaylistSongCrossRef(playlist.id, songId)
                db.playlistSongCrossRef.insertPlaylistSongCrossRef(crossRef)
            }
            Resource.Success(true)
        }
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
            val songs = db.playlistSongDao.getSongsForPlaylist(playlist.id)
            emit(Resource.Success(songs))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage!!))
        } finally {
            emit(Resource.Loading(false))
        }
    }

    override suspend fun deleteSongFromPlaylist(
        playlist: Playlist,
        song: PlaylistSong
    ): Resource<Boolean> = try {
        db.playlistSongDao.deleteSongFromPlaylist(playlist.id, song.songId)
        db.playlistSongDao.deleteSong(song)
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!)
    }

    override suspend fun deletePlaylist(playlist: Playlist): Resource<Boolean> = try {
        db.playlistSongCrossRef.deleteCrossRefsForPlaylist(playlist.id)
        db.playlistSongDao.getSongsForPlaylist(playlist.id).forEach { song ->
            db.playlistSongDao.deleteSong(song)
        }
        db.playlistDao.deletePlaylist(playlist)
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!)
    }

    private suspend fun createFavoritePlaylist() {
        val existingFavorite = db.playlistDao.getPlaylistByName(favoritePlaylist.name)
        if (existingFavorite == null) {
            db.playlistDao.insertPlaylist(favoritePlaylist)
        }
    }

    override suspend fun insertSongToFavorite(song: PlaylistSong) = try {
        val existingSong = db.playlistSongDao.getSongsForPlaylist(2).find {
            it.id == song.id
        }
        if (existingSong != null) {
            Log.d("Result", "From repository the song existed $existingSong")
            Resource.Success(false)
        } else {
            val songId = db.playlistSongDao.insertSong(song)
            Log.d("Result", "From repository with id $songId")
            val existingPlaylist = db.playlistDao.getPlaylistById(2)
            Log.d("Result", "From repository with playlist $existingPlaylist")

            if (existingPlaylist != null) {
                val crossRef = PlaylistSongCrossRef(existingPlaylist.id, songId)
                db.playlistSongCrossRef.insertPlaylistSongCrossRef(crossRef)
                Log.d("Result", "From repository with result $crossRef")
            }
            Resource.Success(true)
        }
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage!!)
    }
}