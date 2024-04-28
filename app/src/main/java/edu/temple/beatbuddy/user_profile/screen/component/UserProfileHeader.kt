package edu.temple.beatbuddy.user_profile.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import edu.temple.beatbuddy.user_discover.screen.component.UserProfileStatsHeader
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.ImageSize

@Composable
fun UserProfileHeader(
    user: User,
    edit: () -> Unit,
    signOut: () -> Unit
) {
    Column {
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
                        if (user.profileImage != "") {
                            val painter = rememberAsyncImagePainter(
                                ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(data = user.profileImage)
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

                    UserProfileStatsHeader(user = user)
                }

                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = user.username,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = user.bio ?: user.fullName,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = edit,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .clip(RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp)
                    .padding(top = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Edit Profile")
            }

            Button(
                onClick = signOut,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp)
                    .padding(top = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Sign Out")
            }
        }
    }
}