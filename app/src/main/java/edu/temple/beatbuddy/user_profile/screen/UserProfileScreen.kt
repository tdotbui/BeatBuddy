package edu.temple.beatbuddy.user_profile.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.temple.beatbuddy.user_auth.model.AuthResult.*
import edu.temple.beatbuddy.user_auth.repository.ProfileViewModel
import edu.temple.beatbuddy.utils.Helpers

@Composable
fun UserProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onSignOut: () -> Unit,
    goToMusicList: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 16.dp)
                    .size(120.dp)
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
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (profileViewModel.currentUserResponse) {
                    is Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White.copy(alpha = 0.3f))
                                .wrapContentSize(Alignment.Center)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    is Success -> {
                        val user = (profileViewModel.currentUserResponse as Success).data
                        Text(
                            text = user.fullName,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    is Error -> {
                        Helpers.showMessage(context, (profileViewModel.currentUserResponse as Error).exception.localizedMessage)
                    }

                    else -> {}
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(Color.White),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to the App!",
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                style = MaterialTheme.typography.titleSmall
            )

            Button(
                onClick = {
                    goToMusicList()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Explore")
            }

            Button(
                onClick = {
                    profileViewModel.signOut()
                    onSignOut()
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Sign Out")
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewHomeScreen() {
//    HomeScreen{}
//}