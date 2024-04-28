package edu.temple.beatbuddy.user_profile.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.temple.beatbuddy.discover.screen.component.UserProfileStatsHeader
import edu.temple.beatbuddy.discover.view_model.ProfileViewModel
import edu.temple.beatbuddy.music_player.view_model.SongViewModel
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.user_profile.screen.component.UserProfileEditingHeader
import edu.temple.beatbuddy.user_profile.screen.component.UserProfileHeader
import edu.temple.beatbuddy.user_profile.view_model.CurrentUserProfileViewModel
import edu.temple.beatbuddy.utils.ImageSize

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CurrentUserProfileScreen(
    songViewModel: SongViewModel,
    currentUserProfileViewModel: CurrentUserProfileViewModel,
    onSignOut: () -> Unit,
) {
    val currentUser by currentUserProfileViewModel.currentUser.collectAsState()
    var isEditing by remember { mutableStateOf(false) }

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
                        songViewModel.releasePlayer()
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
                .background(Color.White),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to the App!",
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}
