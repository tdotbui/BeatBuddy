package edu.temple.beatbuddy.app.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Feed
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Swipe
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material.icons.outlined.Swipe
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.temple.beatbuddy.music_browse.screen.MusicBrowseScreen
import edu.temple.beatbuddy.discover.screen.ProfileListScreen
import edu.temple.beatbuddy.discover.view_model.ProfileViewModel
import edu.temple.beatbuddy.music_player.screen.MusicPlayerScreen
import edu.temple.beatbuddy.music_player.view_model.SongViewModel
import edu.temple.beatbuddy.music_post.screen.FeedsScreen
import edu.temple.beatbuddy.music_post.view_model.SongPostViewModel
import edu.temple.beatbuddy.music_swipe.screen.SwipeSongCardScreen
import edu.temple.beatbuddy.music_swipe.view_model.SwipeSongViewModel
import edu.temple.beatbuddy.user_profile.view_model.CurrentUserProfileViewModel
import edu.temple.beatbuddy.user_profile.screen.CurrentUserProfileScreen
import edu.temple.beatbuddy.utils.Helpers

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(
    songPostViewModel: SongPostViewModel = hiltViewModel(),
    currentUserProfileViewModel: CurrentUserProfileViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    swipeSongViewModel: SwipeSongViewModel = hiltViewModel(),
    songViewModel: SongViewModel = hiltViewModel(),
    goToSignInScreen: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(1) }
    val context = LocalContext.current

    val userState by currentUserProfileViewModel.userState.collectAsState()
    if (userState.user != null) {
        Helpers.saveUidToSharedPreferences(context, userState.user!!.id)
    }

    val currentSong by songViewModel.selectedSong.collectAsState()
    val currentSongList by remember { mutableStateOf(songViewModel.currentSongList) }

    val tabs = listOf(
        TabItem(
            selectedIcon = Icons.Default.Swipe,
            unselectedIcon = Icons.Outlined.Swipe,
            title = "Top's pick"),
        TabItem(
            selectedIcon = Icons.Default.Widgets,
            unselectedIcon = Icons.Outlined.Widgets,
            title = "Browse"),
        TabItem(
            selectedIcon = Icons.Default.AutoAwesome,
            unselectedIcon = Icons.Outlined.AutoAwesome,
            title = "Newsfeed"),
        TabItem(
            selectedIcon = Icons.Default.PersonSearch,
            unselectedIcon = Icons.Outlined.PersonSearch,
            title = "Explore"),
        TabItem(
            selectedIcon = Icons.Default.Person,
            unselectedIcon = Icons.Outlined.Person,
            title = "Profile"),
    )

    Scaffold(
        bottomBar = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                contentColor = Color.Black,
                modifier = Modifier
                    .background(Color.White)
            ) {
                tabs.forEachIndexed { index, item ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Column(
                                modifier = Modifier
                                    .padding(bottom = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Icon(
                                    imageVector = if (selectedTabIndex == index) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title,
                                    modifier = Modifier
                                        .size(24.dp),
                                    tint = if (selectedTabIndex == index) Color.Black else Color.Gray
                                )
                                Text(
                                    text = item.title,
                                    fontSize = 10.sp,
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                )
                            }
                        }
                    )
                }
            }
        },
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            contentAlignment = Alignment.BottomCenter
        ) {
            when (selectedTabIndex) {
                0 -> {
                    SwipeSongCardScreen(swipeSongViewModel = swipeSongViewModel)
                    songViewModel.minimizeScreen()
                }
                1 -> MusicBrowseScreen(
                    songPostViewModel = songPostViewModel,
                    songViewModel = songViewModel
                )
                2 -> {
                    songViewModel.minimizeScreen()
                    FeedsScreen(
                        songPostViewModel = songPostViewModel,
                        songViewModel = songViewModel
                    )
                }
                3 -> {
                    songViewModel.minimizeScreen()
                    ProfileListScreen(
                        profileViewModel = profileViewModel,
                        currentUserProfileViewModel = currentUserProfileViewModel,
                        songPostViewModel = songPostViewModel
                    )
                }
                4 -> {
                    songViewModel.minimizeScreen()
                    currentUserProfileViewModel.fetchCurrentUserStats(false)
                    userState.user?.let { songPostViewModel.fetchPostForUser(it) }
                    CurrentUserProfileScreen(
                        currentUserProfileViewModel = currentUserProfileViewModel,
                        onSignOut = { goToSignInScreen() },
                        songViewModel = songViewModel,
                        songPostViewModel = songPostViewModel
                    )
                }
            }

            if (currentSong != null && currentSongList.size > 1) {
                MusicPlayerScreen(
                    songViewModel = songViewModel,
                    playerEvent = songViewModel,
                )
            }
        }
    }
}

data class TabItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val title: String
)
