package edu.temple.beatbuddy.music_post.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.temple.beatbuddy.music_post.model.MockPost
import edu.temple.beatbuddy.music_post.screen.component.SongPostItem

@Composable
fun FeedsScreen(

) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(MockPost.posts.size) { index ->
                SongPostItem(
                    songPost = MockPost.posts[index],
                    likePost = { }
                ) {

                }
            }
        }
    }
}