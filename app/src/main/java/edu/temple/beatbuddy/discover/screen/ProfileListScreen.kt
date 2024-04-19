package edu.temple.beatbuddy.discover.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.temple.beatbuddy.discover.screen.component.UserProfileItem
import edu.temple.beatbuddy.discover.view_model.AllUsersViewModel
import edu.temple.beatbuddy.user_auth.model.MockUser

@Composable
fun ProfileListScreen(
    allUsersViewModel: AllUsersViewModel = hiltViewModel()
) {
    val users by allUsersViewModel.allUsersState.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn {
            items(users.users.size) { index ->
                UserProfileItem(
                    user = users.users[index],
                    onClick = { /*TODO*/ }) {

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileListPV() {
    ProfileListScreen()
}