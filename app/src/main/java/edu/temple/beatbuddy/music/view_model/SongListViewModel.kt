package edu.temple.beatbuddy.music.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.music.repository.SongListRepository
import edu.temple.beatbuddy.utils.Genre
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val songListRepository: SongListRepository
): ViewModel() {

    private var songListState = MutableStateFlow(SongListState())
        private set

    init {
        getSongsByGenre(songListState.value.selectedGenre)
    }

    fun getSongsByGenre(genre: Int) {
        viewModelScope.launch {
            songListRepository.getSongList(
                fetchFromRemote = true,
                genre = genre
            ).collectLatest { result ->
                when(result) {
                    is Resource.Success -> {
                        result.data?.let { songList ->
                            songListState.update {
                                it.copy(
                                    currentSongList = songList,
                                    selectedGenre = genre,
                                    isLoading = false
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        songListState.update {
                            it.copy(
                                errorMessage = result.message,
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Loading -> {
                        songListState.update {
                            it.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }
}