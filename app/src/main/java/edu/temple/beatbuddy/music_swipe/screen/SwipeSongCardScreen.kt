package edu.temple.beatbuddy.music_swipe.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.temple.beatbuddy.app.screen.HomeScreen
import edu.temple.beatbuddy.component.ImageFactory
import edu.temple.beatbuddy.discover.screen.component.UserProfileItem
import edu.temple.beatbuddy.music_post.model.MockPost
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.screen.component.UserPostHeader
import edu.temple.beatbuddy.music_swipe.screen.component.SwipeableCard
import edu.temple.beatbuddy.music_swipe.view_model.SwipeSongViewModel
import edu.temple.beatbuddy.user_auth.model.MockUser
import edu.temple.beatbuddy.user_auth.model.User

@Composable
fun SwipeSongCardScreen(
    swipeSongViewModel: SwipeSongViewModel
) {
    val posts by swipeSongViewModel.songPostState.collectAsState()

    SwipeableCard(
        onDismiss = {

        }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            SongCard(
                songPost = posts.posts[0]
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SwipeScreenPV() {
//    SwipeSongCardScreen()
//}

@Composable
fun SongCard(
    songPost: SongPost
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .height(420.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserPostHeader(user = songPost.user ?: User())

            ImageFactory(
                context = LocalContext.current,
                imageUrl = songPost.songImage,
                imageVector = Icons.Default.ImageNotSupported,
                description = songPost.title,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = songPost.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = songPost.artistName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}