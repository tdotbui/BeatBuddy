package edu.temple.beatbuddy.music_browse.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.music_browse.repository.SongListRepository
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

    var songListState = MutableStateFlow(SongListState())
        private set

    init {
        getSongsByGenre(Genre.POP)
    }

    fun getSongsByGenre(genre: Genre) {
        songListState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            songListRepository.getSongList(
                fetchFromRemote = !songListState.value.genres.contains(genre),
                genre = genre.id
            ).collectLatest { result ->
                when(result) {
                    is Resource.Success -> {
                        result.data?.let { songList ->
                            songListState.update {
                                if (!it.genres.contains(genre)) it.genres.add(genre)
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
                    else -> {}
                }
            }
        }
    }
}