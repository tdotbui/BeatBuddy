package edu.temple.beatbuddy.discover.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.temple.beatbuddy.discover.screen.component.UserProfileHeader
import edu.temple.beatbuddy.discover.view_model.ProfileViewModel
import edu.temple.beatbuddy.user_auth.model.MockUser
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.ImageSize
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun UserProfileScreen(
    profileViewModel: ProfileViewModel,
    back: (User) -> Unit
) {
    val currentUser by profileViewModel.currentUser.collectAsState()
    val isFollowing = profileViewModel.isFollowing.asStateFlow()

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(ImageSize.large)
                            .clip(CircleShape)
                            .background(color = Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "User Image",
                            color = Color.Black,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = currentUser.username,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = currentUser.fullName,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp
                        )
                    }
                }

                UserProfileHeader(user = currentUser)
            }

            Button(
                onClick = {
                    if (isFollowing.value) {
                        profileViewModel.unfollowCurrent()
                    } else {
                        profileViewModel.followCurrent()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFollowing.value) Color.White else MaterialTheme.colorScheme.primary,
                    contentColor = if (isFollowing.value) Color.Black else MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = if (isFollowing.value) "Unfollow" else "Follow")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color.White),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    back(currentUser)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp)
            ) {
                Text(text = "Back")
            }
        }
    }
}
