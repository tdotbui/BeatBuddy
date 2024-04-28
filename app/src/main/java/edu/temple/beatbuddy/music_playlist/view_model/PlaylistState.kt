package edu.temple.beatbuddy.music_playlist.view_model

import edu.temple.beatbuddy.music_playlist.model.Playlist
import edu.temple.beatbuddy.music_playlist.model.PlaylistSong

data class PlaylistState(
    val isLoading: Boolean = false,

    val selectedPlaylist: Playlist = Playlist(0L, "Favorite", "https://cdn4.iconfinder.com/data/icons/ui-for-multimedia-players/48/21_favorite_music_favorite_list_note_love_favorite_heart_like_music_musical_notes_song_audio_tone_dot-1024.png"),
    val currentSongList: List<PlaylistSong> = emptyList(),
    val playlists: List<Playlist> = emptyList(),

    val errorMessage: String? = null
)