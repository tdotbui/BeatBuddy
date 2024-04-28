package edu.temple.beatbuddy.music_post.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import edu.temple.beatbuddy.user_discover.screen.component.ProfilePicture
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.ImageSize

@Composable
fun UserPostHeader(
    user: User
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfilePicture(
                userProfile = user.profileImage ?: "",
                size = ImageSize.xSmall
            )

            Text(
                text = user.username,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}