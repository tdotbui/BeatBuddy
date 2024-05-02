package edu.temple.beatbuddy.user_profile.screen.component

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import edu.temple.beatbuddy.app.screen.HomeScreen
import edu.temple.beatbuddy.component.ImageFactory
import edu.temple.beatbuddy.user_auth.model.MockUser
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.ImageSize

@Composable
fun UserProfileEditingHeader(
    user: User,
    onEdit: (String, String, String, Boolean) -> Unit
) {
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    var currentImage = user.profileImage

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImage = uri
        }
    )
    var username by remember { mutableStateOf(user.username) }
    var bio by remember { mutableStateOf(user.bio) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (selectedImage != null) {
            val painter = rememberAsyncImagePainter(
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(data = selectedImage)
                    .build()
            )

            Box(
                modifier = Modifier
                    .size(ImageSize.large)
                    .clip(CircleShape)
                    .background(color = Color.LightGray)
                    .clickable {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painter,
                    contentDescription = "profile image",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.size(ImageSize.large)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(ImageSize.large)
                    .clip(CircleShape)
                    .background(color = Color.LightGray)
                    .clickable {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (currentImage != null) {
                    val painter = rememberAsyncImagePainter(
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(data = currentImage)
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
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.wrapContentWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Your username",
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )

                Text(
                    text = "Your bio",
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                BasicTextField(
                    value = username,
                    onValueChange = { username = it },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .clip(RectangleShape)
                )
                Divider()
                BasicTextField(
                    value = bio ?: "enter your bio",
                    onValueChange = { bio = it },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    maxLines = 3
                )
                Divider()
            }
        }

        Button(
            onClick = {
                onEdit(selectedImage.toString(), username, bio ?: "", selectedImage != null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = "Finish")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun EditingScreenPV() {
//    UserProfileEditingHeader(user = MockUser.users[0])
//}