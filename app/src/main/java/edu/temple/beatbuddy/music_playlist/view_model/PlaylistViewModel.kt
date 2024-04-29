package edu.temple.beatbuddy.music_playlist.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.music_browse.model.Song
import edu.temple.beatbuddy.music_browse.model.mapping.toPlaylistSong
import edu.temple.beatbuddy.music_playlist.model.Playlist
import edu.temple.beatbuddy.music_playlist.model.PlaylistSong
import edu.temple.beatbuddy.music_playlist.repository.PlaylistRepository
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository
): ViewModel() {

    var playlistState = MutableStateFlow(PlaylistState())
        private set

    var currentSong = MutableStateFlow(PlaylistSong())
        private set

    init {
        fetchPlaylists()
    }

    fun setSong(song: Song) {
        currentSong.value = song.toPlaylistSong()
    }

    fun insertSong(playlist: Playlist) = viewModelScope.launch {
        playlistRepository.insertSongToPlayList(playlist =  playlist, song = currentSong.value).let {result ->
            if (result is Resource.Success) {
                fetchPlaylists()
            }
        }
    }

    fun deleteSong(song: PlaylistSong) = viewModelScope.launch {
        val playlist = playlistState.value.selectedPlaylist
        playlistRepository.deleteSongFromPlaylist(playlist = playlist, song = song).let {result ->
            if (result is Resource.Success) {
                fetchSongsFromPlaylist(playlist)
            }
        }
    }

    fun deletePlaylist(playlist: Playlist) = viewModelScope.launch {
        val index = playlistState.value.playlists.indexOf(playlist)
        if (index > 0) {
            val previousPlaylist = playlistState.value.playlists[index - 1]

            playlistRepository.deletePlaylist(playlist).let {result ->
                if (result is Resource.Success) {
                    fetchSongsFromPlaylist(previousPlaylist)
                    fetchPlaylists()
                }
            }
        } else {
            playlistState.update { it.copy(errorMessage = "Cannot delete Favorite playlist.") }
        }
    }

    private fun fetchPlaylists() = viewModelScope.launch {
        playlistState.update {
            it.copy(isLoading = true)
        }
        playlistRepository.getAllPlaylists().let { result ->
            when(result) {
                is Resource.Success -> {
                    result.data?.let { playlists ->
                        val list = playlists.drop(1)
                        playlistState.update {
                            it.copy(
                                playlists = list,
                                selectedPlaylist = list.first(),
                                isLoading = false
                            )
                        }
                        if (playlistState.value.playlists.isNotEmpty()) {
                            fetchSongsFromPlaylist(playlistState.value.playlists.first())
                        }
                    }
                }
                is Resource.Error -> {
                    playlistState.update {
                        it.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                }
                else -> {}
            }
        }
    }
    fun fetchSongsFromPlaylist(playlist: Playlist) = viewModelScope.launch {
        playlistState.update {
            it.copy(isLoading = true)
        }
        playlistRepository.getAllSongsFromPlayList(playlist).collectLatest { result ->
            when(result) {
                is Resource.Success -> {
                    result.data?.let { songList ->
                        playlistState.update {
                            it.copy(
                                selectedPlaylist = playlist,
                                currentSongList = songList,
                                isLoading = false
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    playlistState.update {
                        it.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                }
                else -> {}
            }
        }
    }
}