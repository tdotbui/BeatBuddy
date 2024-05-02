package edu.temple.beatbuddy.music_playlist.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.temple.beatbuddy.music_playlist.model.Playlist
import edu.temple.beatbuddy.music_playlist.model.PlaylistSong
import edu.temple.beatbuddy.music_playlist.model.PlaylistSongCrossRef

@Database(
    entities = [Playlist::class, PlaylistSong::class, PlaylistSongCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class PlaylistDatabase: RoomDatabase() {
    abstract val playlistDao: PlaylistDao
    abstract val playlistSongDao: PlaylistSongDao
    abstract val playlistSongCrossRef: PlaylistSongCrossRefDao
}