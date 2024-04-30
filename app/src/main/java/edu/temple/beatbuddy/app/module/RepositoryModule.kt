package edu.temple.beatbuddy.app.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.temple.beatbuddy.music_browse.repository.SongListRepository
import edu.temple.beatbuddy.music_browse.repository.SongListRepositoryImpl
import edu.temple.beatbuddy.music_playlist.repository.PlaylistRepository
import edu.temple.beatbuddy.music_playlist.repository.PlaylistRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSongRepository(
        songListRepositoryImpl: SongListRepositoryImpl
    ): SongListRepository

    @Binds
    @Singleton
    abstract fun bindPlaylistRepository(
        playlistRepositoryImpl: PlaylistRepositoryImpl
    ): PlaylistRepository
}