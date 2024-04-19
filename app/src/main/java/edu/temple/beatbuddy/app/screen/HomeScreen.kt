package edu.temple.beatbuddy.app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feed
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Swipe
import androidx.compose.material.icons.filled.Widgets
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.temple.beatbuddy.music_browse.screen.MusicBrowseScreen
import edu.temple.beatbuddy.discover.screen.ProfileListScreen
import edu.temple.beatbuddy.music_post.screen.FeedsScreen
import edu.temple.beatbuddy.user_profile.screen.UserProfileScreen

@Composable
fun HomeScreen() {
    var selectedTabIndex by remember { mutableIntStateOf(1) }

    val tabs = listOf(
        TabItem(Icons.Default.Swipe, "Top's pick"),
        TabItem(Icons.Default.Widgets, "Browse"),
        TabItem(Icons.Default.Feed, "Feeds"),
        TabItem(Icons.Default.Search, "Explore"),
        TabItem(Icons.Default.Person, "Profile"),
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
                                    imageVector = item.icon,
                                    contentDescription = "Swipe icon",
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Black
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
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (selectedTabIndex) {
                0 -> TabScreen1()
                1 -> MusicBrowseScreen()
                2 -> FeedsScreen()
                3 -> ProfileListScreen()
                4 -> UserProfileScreen(onSignOut = {})
            }
        }
    }
}

data class TabItem(
    val icon: ImageVector,
    val title: String
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPV() {
    HomeScreen()
}

@Composable
fun TabScreen1() {
    Text(text = "This is Tab 1")
}

@Composable
fun TabScreen3() {
    Text(text = "This is Tab 3")
}
