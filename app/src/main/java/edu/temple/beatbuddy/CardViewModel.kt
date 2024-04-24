package edu.temple.beatbuddy

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ViewModel for managing card navigation and providing data in a UI that resembles a card stack.
class CardViewModel : ViewModel() {
    // Private mutable state flow to track the current index in the card data list.
    private val _currentIndex = MutableStateFlow(0)

    // Publicly exposed read-only state flow of the current index.
    val currentIndex: StateFlow<Int> = _currentIndex

    // Maximum index value calculated based on the size of dummy card data.
    private val maxIndex = getDummyCardData().size - 1

    // Method to update the current index based on the swipe direction ("left" or "right").
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

    // Provides a static list of dummy card data for demonstration purposes.
    // This simulates loading card content such as songs from a more dynamic data source.
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