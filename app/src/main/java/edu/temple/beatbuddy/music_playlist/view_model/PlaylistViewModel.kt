package edu.temple.beatbuddy.music_playlist.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.music_playlist.model.Playlist
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

    init {
        fetchPlaylists()
        fetchSongsFromPlaylist(playlistState.value.selectedPlaylist)
    }

    fun fetchPlaylists() = viewModelScope.launch {
        playlistState.update {
            it.copy(isLoading = true)
        }
        playlistRepository.getAllPlaylists().collectLatest { result ->
            when(result) {
                is Resource.Success -> {
                    result.data?.let { playlists ->
                        playlistState.update {
                            it.copy(
                                playlists = playlists,
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