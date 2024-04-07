package edu.temple.beatbuddy.music.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.temple.beatbuddy.music.repository.SongListRepository
import edu.temple.beatbuddy.utils.Genre
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val songListRepository: SongListRepository
): ViewModel() {

    init {
        getSongs(Genre.POP)
    }

    private fun getSongs(genre: Int) {
        viewModelScope.launch {
            songListRepository.getSongList(true, genre)
                .also { response ->
                    Log.d("Result", response.toString())
                }
        }
    }
}