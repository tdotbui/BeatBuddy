package edu.temple.beatbuddy.discover.screen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.temple.beatbuddy.discover.screen.component.UserProfileItem
import edu.temple.beatbuddy.discover.view_model.AllUsersViewModel
import edu.temple.beatbuddy.discover.view_model.ProfileViewModel
import edu.temple.beatbuddy.user_auth.model.MockUser
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileListScreen(
    allUsersViewModel: AllUsersViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val users by allUsersViewModel.allUsersState.collectAsState()
    var selectedUser by remember { mutableStateOf(User()) }
    val scope = rememberCoroutineScope()
    var selectedUserIndex by remember { mutableIntStateOf(0) }

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)

    BottomSheetScaffold(
        sheetContent = {
            UserProfileScreen(
                user = selectedUser,
                back = {
                    scope.launch {
                        allUsersViewModel.fetchAllUsers()
                        bottomSheetState.hide()
                    }
                }
            )
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetShadowElevation = 5.dp,
        sheetContainerColor = Color.White,
        sheetPeekHeight = 0.dp,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier.padding(paddingValue)
        ) {
            items(users.users.size) { index ->
                UserProfileItem(
                    user = users.users[index],
                    onClick = { user ->
                        selectedUserIndex = index
                        selectedUser = user
                        scope.launch {
                            bottomSheetState.expand()
                        }
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileListPV() {
    ProfileListScreen()
}