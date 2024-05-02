package edu.temple.beatbuddy.user_discover.screen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.ImageSize

@Composable
fun UserProfileItem(
    user: User,
    onClick: (User) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clickable { onClick(user) },
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                ProfilePicture(
                    userProfile = user.profileImage ?: "",
                    size = ImageSize.small
                )

                Column(
                    modifier = Modifier
                        .padding(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = user.username,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Text(
                        text = user.fullName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ProfileItemPV() {
//    UserProfileItem(
//        user = MockUser.users[1],
//        onClick = {}) {}
//}