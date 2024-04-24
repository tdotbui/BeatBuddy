package edu.temple.beatbuddy.music_browse.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.Timestamp
import com.google.firebase.ktx.Firebase
import edu.temple.beatbuddy.discover.screen.UserProfileScreen
import edu.temple.beatbuddy.music_browse.model.local.Song
import edu.temple.beatbuddy.music_browse.screen.component.GenreItem
import edu.temple.beatbuddy.music_browse.screen.component.SongRowItem
import edu.temple.beatbuddy.music_browse.view_model.SongListViewModel
import edu.temple.beatbuddy.music_player.MusicPlayerFullScreen
import edu.temple.beatbuddy.music_player.MusicPlayerSmallScreen
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.screen.component.NewPostDialog
import edu.temple.beatbuddy.music_post.view_model.SongPostViewModel
import edu.temple.beatbuddy.utils.Genre
import edu.temple.beatbuddy.utils.Helpers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MusicBrowseScreen(
    songListViewModel: SongListViewModel = hiltViewModel(),
    songPostViewModel: SongPostViewModel
) {
    val context = LocalContext.current
    val songs by songListViewModel.songListState.collectAsState()
    var openDialog by remember { mutableStateOf(false) }
    var songImage by remember { mutableStateOf("") }
    var songTitle by remember { mutableStateOf("") }
    var selectedSong by remember { mutableStateOf<Song?>(null) }

    var isFullScreen by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)

    BottomSheetScaffold(
        sheetContent = {
            selectedSong?.let {
                if (isFullScreen) {
                    MusicPlayerFullScreen(
                        song = it,
                        close = {
                            isFullScreen = false
//                            scope.launch {
//                                bottomSheetState.hide()
//                            }
                        }
                    )
                } else {
                    MusicPlayerSmallScreen(
                        expand = {
                            isFullScreen = true
                        }
                    )
                }
            }
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
                .padding(paddingValue)
                .fillMaxWidth()
        ) {
            LazyRow(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                userScrollEnabled = true
            ) {
                items(Genre.entries.size) { index ->
                    val genre = Genre.entries[index]
                    GenreItem(
                        genre = genre,
                        onClick = { songListViewModel.getSongsByGenre(genre.id) }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp),
                ) {
                    val songList = songs.currentSongList
                    items(songList.size) { index ->
                        SongRowItem(
                            song = songList[index],
                            onMusicClick = { song ->
                                scope.launch {
                                    bottomSheetState.expand()
                                    selectedSong = song
                                }
                            },
                            shareClick = { song ->
                                openDialog = true
                                songImage = song.album.cover_medium ?: ""
                                songTitle = song.title
                                selectedSong = song
                            }
                        )
                    }
                }

                if (songs.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
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