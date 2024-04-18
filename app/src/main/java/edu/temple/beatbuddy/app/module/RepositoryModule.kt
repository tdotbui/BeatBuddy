package edu.temple.beatbuddy.app.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.temple.beatbuddy.music.repository.SongListRepository
import edu.temple.beatbuddy.music.repository.SongListRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSongRepository(
        songListRepositoryImpl: SongListRepositoryImpl
    ): SongListRepository

}