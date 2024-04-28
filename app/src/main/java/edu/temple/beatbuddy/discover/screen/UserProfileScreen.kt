package edu.temple.beatbuddy.discover.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import edu.temple.beatbuddy.discover.screen.component.UserProfileStatsHeader
import edu.temple.beatbuddy.discover.view_model.ProfileViewModel
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.user_profile.view_model.CurrentUserProfileViewModel
import edu.temple.beatbuddy.utils.ImageSize
import kotlinx.coroutines.flow.asStateFlow

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun UserProfileScreen(
    profileViewModel: ProfileViewModel,
    currentUserProfileViewModel: CurrentUserProfileViewModel,
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
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(ImageSize.large)
                                .clip(CircleShape)
                                .background(color = Color.LightGray)
                                .border(width = 0.5.dp, color = Color.White, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (currentUser.profileImage != "") {
                                val painter = rememberAsyncImagePainter(
                                    ImageRequest
                                        .Builder(LocalContext.current)
                                        .data(data = currentUser.profileImage)
                                        .build()
                                )

                                Image(
                                    painter = painter,
                                    contentDescription = "profile image",
                                    contentScale = ContentScale.FillHeight,
                                    modifier = Modifier.size(ImageSize.large)
                                )
                            } else {
                                Text(
                                    text = "User Image",
                                    color = Color.Black,
                                    style = MaterialTheme.typography.titleSmall,
                                )
                            }
                        }

                        UserProfileStatsHeader(user = currentUser)
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
                            text = currentUser.bio ?: currentUser.fullName,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (isFollowing.value) {
                            profileViewModel.unfollowCurrent()
                        } else {
                            profileViewModel.followCurrent()
                        }
                        currentUserProfileViewModel.fetchCurrentUserStats(true)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .clip(RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFollowing.value) Color.White else MaterialTheme.colorScheme.primary,
                        contentColor = if (isFollowing.value) Color.Black else MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = if (isFollowing.value) "Unfollow" else "Follow")
                }

                Button(
                    onClick = {
                        back(currentUser)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Back")
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}
