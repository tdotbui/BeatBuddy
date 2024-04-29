package edu.temple.beatbuddy.music_browse.screen

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.Timestamp
import edu.temple.beatbuddy.music_browse.model.Song
import edu.temple.beatbuddy.music_browse.screen.component.GenreItem
import edu.temple.beatbuddy.music_browse.screen.component.PlaylistItem
import edu.temple.beatbuddy.music_browse.screen.component.PlaylistSongRowItem
import edu.temple.beatbuddy.music_browse.screen.component.SongRowItem
import edu.temple.beatbuddy.music_browse.view_model.SongListViewModel
import edu.temple.beatbuddy.music_player.view_model.SongViewModel
import edu.temple.beatbuddy.music_playlist.model.Playlist
import edu.temple.beatbuddy.music_playlist.screen.AddToPlaylistScreen
import edu.temple.beatbuddy.music_playlist.view_model.PlaylistViewModel
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.screen.component.NewPostDialog
import edu.temple.beatbuddy.music_post.view_model.SongPostViewModel
import edu.temple.beatbuddy.user_discover.screen.UserProfileScreen
import edu.temple.beatbuddy.utils.Genre
import edu.temple.beatbuddy.utils.Helpers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MusicBrowseScreen(
    songListViewModel: SongListViewModel = hiltViewModel(),
    songPostViewModel: SongPostViewModel,
    songViewModel: SongViewModel,
    playlistViewModel: PlaylistViewModel
) {
    val context = LocalContext.current
    val browseSongs by songListViewModel.songListState.collectAsState()
    var openDialog by remember { mutableStateOf(false) }
    var songImage by remember { mutableStateOf("") }
    var songTitle by remember { mutableStateOf("") }
    var selectedSong by remember { mutableStateOf<Song?>(null) }

    val scrollState = rememberLazyListState()
    LaunchedEffect(browseSongs.selectedGenre) {
        val index = Genre.entries.indexOf(browseSongs.selectedGenre)
        if (index != -1) {
            scrollState.scrollToItem(index)
        }
    }

    val isViewingGenre by songViewModel.isViewingGenre.collectAsState()
    val playlistSongs by playlistViewModel.playlistState.collectAsState()
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)
    val scope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedPlaylist by remember { mutableStateOf(Playlist(0L, "")) }

    BottomSheetScaffold(
        sheetContent = {
            AddToPlaylistScreen(
                playlistViewModel = playlistViewModel,
                finish = {
                    scope.launch { bottomSheetState.hide() }
                }
            )
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetShadowElevation = 5.dp,
        sheetContainerColor = Color.White,
        sheetPeekHeight = 0.dp,
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            if (isViewingGenre) {
                LazyRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    userScrollEnabled = true,
                    state = scrollState
                ) {
                    items(Genre.entries.size) { index ->
                        val genre = Genre.entries[index]
                        GenreItem(
                            genre = genre,
                            onClick = { songListViewModel.getSongsByGenre(genre) },
                            isSelected = genre == browseSongs.selectedGenre
                        )
                    }
                }
            } else {
                LazyRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    userScrollEnabled = true,
                    state = scrollState
                ) {
                    val playlists = playlistSongs.playlists
                    items(playlists.size) { index ->
                        val playlist = playlists[index]
                        PlaylistItem(
                            playlist = playlist,
                            onClick = { playlistViewModel.fetchSongsFromPlaylist(playlist) },
                            isSelected = playlist.id == playlistSongs.selectedPlaylist.id,
                            onLongPress = {playlist ->
                                showDeleteDialog = true
                                selectedPlaylist = playlist
                            }
                        )
                    }
                }
            }

            Divider(
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .heightIn(30.dp)
                        .width(150.dp)
                        .clickable {
                            songViewModel.viewingGenres("Genres")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Genres",
                        fontSize = 14.sp,
                        fontWeight = if (isViewingGenre) FontWeight.SemiBold else FontWeight.Light,
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
                            songViewModel.viewingGenres("Playlists")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your Playlists",
                        fontSize = 14.sp,
                        fontWeight = if (isViewingGenre) FontWeight.Light else FontWeight.SemiBold,
                    )
                }
            }

            Divider(
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                if (isViewingGenre) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 16.dp),
                    ) {
                        val songList = browseSongs.currentSongList
                        items(songList.size) { index ->
                            SongRowItem(
                                song = songList[index],
                                onMusicClick = { song ->
                                    songViewModel.setUpSongLists(songList)
                                    songViewModel.onSongClick(song)
                                    selectedSong = song
                                },
                                shareClick = { song ->
                                    openDialog = true
                                    songImage = song.album.cover_medium ?: ""
                                    songTitle = song.title
                                    selectedSong = song
                                },
                                addClick = { song ->
                                    playlistViewModel.setSong(song)
                                    scope.launch {
                                        bottomSheetState.expand()
                                    }
                                }
                            )
                        }
                    }
                } else {
                    val songList = playlistSongs.currentSongList
                    if (songList.isEmpty()) {
                        Text(text = "No song has been added yet")
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 16.dp),
                    ) {
                        items(songList.size) { index ->
                            PlaylistSongRowItem(
                                playlistSong = songList[index],
                                onMusicClick = { song ->
//                                songViewModel.setUpSongLists(songList)
//                                songViewModel.onSongClick(song)
//                                selectedSong = song
                                },
                                delete = {song ->
                                    playlistViewModel.deleteSong(song)
                                }
                            )
                        }
                    }
                }

                if (browseSongs.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text(text = "Delete Playlist") },
                    text = { Text(text = "Are you sure you want to delete playlist ${selectedPlaylist.name}?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                playlistViewModel.deletePlaylist(selectedPlaylist)
                                showDeleteDialog = false
                            }
                        ) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }
                        ) {
                            Text("No")
                        }
                    }
                )
            }

            if (playlistSongs.errorMessage != null) {
                Helpers.showMessage(context, playlistSongs.errorMessage)
                playlistSongs.errorMessage = null
            }

            if (openDialog) {
                NewPostDialog(
                    context = context,
                    imageUrl = songImage,
                    title = songTitle,
                    dismissDialog = { openDialog = false },
                    makePost = { caption ->
                        val songPost = SongPost(
                            postId = "",
                            ownerUid = Helpers.getUidFromSharedPreferences(context) ?: "",
                            caption = caption,
                            likes = 0,
                            timestamp = Timestamp.now(),
                            songId = selectedSong?.id ?: 0L,
                            title = selectedSong?.title ?: "",
                            preview = selectedSong?.preview ?: "",
                            artistName = selectedSong?.artist?.name ?: "",
                            artistPicture = selectedSong?.artist?.picture_medium ?: "",
                            songImage = selectedSong?.album?.cover_medium ?: "",
                            user = null
                        )
                        songPostViewModel.makePost(songPost)
                    }
                )
            }
        }
    }
}