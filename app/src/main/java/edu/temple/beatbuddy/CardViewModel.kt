package edu.temple.beatbuddy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CardViewModel : ViewModel() {
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex
    private val maxIndex = getDummyCardData().size - 1

    fun moveToNextCard(dismissDirection: String) {
        when (dismissDirection) {
            "right" -> if (_currentIndex.value < maxIndex) {
                _currentIndex.value++
            }
            "left" -> if (_currentIndex.value > 0) {
                _currentIndex.value--
            }
        }
    }

    fun getDummyCardData(): List<Card> {
        return listOf(
            Card("Song One", "Artist A"),
            Card("Song Two", "Artist B"),
            Card("Song Three", "Artist C"),
            Card("Song Four", "Artist D"),
            Card("Song Five", "Artist E")
        )
    }
}