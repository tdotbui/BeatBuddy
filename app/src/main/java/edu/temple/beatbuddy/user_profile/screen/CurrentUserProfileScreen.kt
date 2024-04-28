package edu.temple.beatbuddy.user_profile.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.temple.beatbuddy.music_player.view_model.SongViewModel
import edu.temple.beatbuddy.music_post.screen.component.SongPostRowItem
import edu.temple.beatbuddy.music_post.view_model.SongPostViewModel
import edu.temple.beatbuddy.user_profile.screen.component.UserProfileEditingHeader
import edu.temple.beatbuddy.user_profile.screen.component.UserProfileHeader
import edu.temple.beatbuddy.user_profile.view_model.CurrentUserProfileViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CurrentUserProfileScreen(
    songViewModel: SongViewModel,
    currentUserProfileViewModel: CurrentUserProfileViewModel,
    songPostViewModel: SongPostViewModel,
    onSignOut: () -> Unit,
) {
    val currentUser by currentUserProfileViewModel.currentUser.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    val posts by songPostViewModel.userSongPostState.collectAsState()

    LaunchedEffect(Unit) {
        songPostViewModel.fetchPostForUser(currentUser)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!isEditing) {
                UserProfileHeader(
                    user = currentUser,
                    edit = {
                        isEditing = true
                    },
                    signOut = {
                        songViewModel.stop()
                        currentUserProfileViewModel.signOut()
                        onSignOut()
                    }
                )
            } else {
                UserProfileEditingHeader(user = currentUser) { image, username, bio, shouldUpdate ->
                    Log.d("Image", image)
                    currentUserProfileViewModel.updateUserProfile(image, username, bio, shouldUpdate)
                    isEditing = false
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (posts.posts.isEmpty()) {
                Text(
                    text = "Welcome to the App!",
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                    style = MaterialTheme.typography.titleSmall
                )
            } else {
                if (posts.isLoading) {
                    CircularProgressIndicator()
                } else {
                    LazyColumn {
                        items(posts.posts.size) {index ->
                            val post = posts.posts[index]
                            SongPostRowItem(
                                songPost = post,
                                songPostViewModel = songPostViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
