package edu.temple.beatbuddy.user_profile.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.temple.beatbuddy.music_browse.screen.component.MenuItem
import edu.temple.beatbuddy.music_browse.screen.component.PlaylistItem
import edu.temple.beatbuddy.music_player.view_model.SongViewModel
import edu.temple.beatbuddy.music_playlist.model.Playlist
import edu.temple.beatbuddy.music_playlist.view_model.PlaylistViewModel
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.screen.component.SongPostRowItem
import edu.temple.beatbuddy.music_post.view_model.SongPostViewModel
import edu.temple.beatbuddy.user_profile.screen.component.UserProfileEditingHeader
import edu.temple.beatbuddy.user_profile.screen.component.UserProfileHeader
import edu.temple.beatbuddy.user_profile.view_model.CurrentUserProfileViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CurrentUserProfileScreen(
    songViewModel: SongViewModel,
    currentUserProfileViewModel: CurrentUserProfileViewModel,
    songPostViewModel: SongPostViewModel,
    playlistViewModel: PlaylistViewModel,
    onSignOut: () -> Unit,
) {
    val currentUser by currentUserProfileViewModel.currentUser.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    val posts by songPostViewModel.userSongPostState.collectAsState()

    LaunchedEffect(Unit) {
        songPostViewModel.fetchPostForUser(currentUser)
    }

    val isViewingPosts by currentUserProfileViewModel.isViewingPosts.collectAsState()
    val playlistSongs by playlistViewModel.playlistState.collectAsState()

    var showDeletePostDialog by remember { mutableStateOf(false) }
    var selectedSongPost by remember { mutableStateOf(SongPost()) }

    var showDeletePlaylistDialog by remember { mutableStateOf(false) }
    var selectedPlaylist by remember { mutableStateOf(Playlist(0L, "")) }

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
            if (!isEditing) {
                UserProfileHeader(
                    user = currentUser,
                    edit = {
                        isEditing = true
                    },
                    signOut = {
                        songViewModel.stop()
                        currentUserProfileViewModel.signOut()
                        onSignOut()
                    }
                )
            } else {
                UserProfileEditingHeader(user = currentUser) { image, username, bio, shouldUpdate ->
                    Log.d("Image", image)
                    currentUserProfileViewModel.updateUserProfile(image, username, bio, shouldUpdate)
                    isEditing = false
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .heightIn(30.dp)
                        .width(150.dp)
                        .clickable {
                            currentUserProfileViewModel.viewingPosts("Posts")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Posts",
                        fontSize = 14.sp,
                        fontWeight = if (isViewingPosts) FontWeight.SemiBold else FontWeight.Light,
                    )
                }

                Divider(
                    modifier = Modifier
                        .height(30.dp)
                        .width(1.dp),
                )

                Box(
                    modifier = Modifier
                        .heightIn(30.dp)
                        .width(150.dp)
                        .clickable {
                            currentUserProfileViewModel.viewingPosts("Playlists")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your Playlists",
                        fontSize = 14.sp,
                        fontWeight = if (isViewingPosts) FontWeight.Light else FontWeight.SemiBold,
                    )
                }
            }

            Divider(Modifier.padding(bottom = 8.dp))

            if (isViewingPosts) {
                if (posts.posts.isEmpty()) {
                    Text(
                        text = "Welcome to the App!",
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                        style = MaterialTheme.typography.titleSmall
                    )
                } else {
                    if (posts.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        LazyColumn {
                            items(posts.posts.size) {index ->
                                val post = posts.posts[index]
                                SongPostRowItem(
                                    songPost = post,
                                    songPostViewModel = songPostViewModel,
                                    onLongPress = {songPost ->
                                        selectedSongPost = songPost
                                        showDeletePostDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    val playlists = playlistSongs.playlists
                    val currentPlaylist = playlistSongs.currentSongList
                    items(playlists.size) { index ->
                        val playlist = playlists[index]
                        PlaylistItem(
                            playlist = playlist,
                            onClick = {
                                playlistViewModel.fetchSongsFromPlaylist(playlist)
                                      },
                            isSelected = playlist.id == playlistSongs.selectedPlaylist.id,
                            onLongPress = {
                                selectedPlaylist = it
                                playlistViewModel.fetchSongsFromPlaylist(selectedPlaylist)
                            },
                            onPlayClick = {
                                songViewModel.setUpSongLists(currentPlaylist)
                                songViewModel.onSongClick(currentPlaylist[0])

                            },
                            openDialog = {result ->
                                showDeletePlaylistDialog = result
                            }
                        )
                    }
                }
            }

            if (showDeletePlaylistDialog) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text(text = "Delete Playlist") },
                    text = { Text(text = "Are you sure you want to delete playlist ${selectedPlaylist.name}?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                playlistViewModel.deletePlaylist(selectedPlaylist)
                                showDeletePlaylistDialog = false
                            }
                        ) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeletePlaylistDialog = false }
                        ) {
                            Text("No")
                        }
                    }
                )
            }

            if (showDeletePostDialog) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text(text = "Delete Post") },
                    text = { Text(text = "Are you sure you want to delete post about the song ${selectedSongPost.title}?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                songPostViewModel.deletePost(selectedSongPost, currentUser)
                                showDeletePostDialog = false
                            }
                        ) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeletePostDialog = false }
                        ) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
}
