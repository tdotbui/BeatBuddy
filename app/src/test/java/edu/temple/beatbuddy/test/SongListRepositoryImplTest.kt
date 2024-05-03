package edu.temple.beatbuddy.test

import edu.temple.beatbuddy.music_browse.model.Song
import edu.temple.beatbuddy.music_browse.model.local.SongDao
import edu.temple.beatbuddy.music_browse.model.local.SongDatabase
import edu.temple.beatbuddy.music_browse.model.remote.AlbumDto
import edu.temple.beatbuddy.music_browse.model.remote.ArtistDto
import edu.temple.beatbuddy.music_browse.model.remote.SongApi
import edu.temple.beatbuddy.music_browse.model.remote.SongDto
import edu.temple.beatbuddy.music_browse.model.remote.SongListDto
import edu.temple.beatbuddy.music_browse.repository.SongListRepositoryImpl
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.test.runBlockingTest
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify
import org.mockito.kotlin.any
import kotlinx.coroutines.flow.toList
import org.junit.Test
import org.junit.runner.RunWith

// Define the test class using MockitoJUnitRunner to enable Mockito annotations
@RunWith(MockitoJUnitRunner::class)
class SongListRepositoryImplTest {

    @Mock
    private lateinit var songApi: SongApi // Mock the SongApi to simulate API calls
    @Mock
    private lateinit var songDatabase: SongDatabase // Mock the SongDatabase to simulate database interactions
    @Mock
    private lateinit var songDao: SongDao // Mock the SongDao to simulate DAO operations

    // Test to verify local data fetching when local database is not empty
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getSongList fetches from local when not empty and fetchFromRemote is false`() = runBlockingTest {
        // Setup: Create a mocked Song instance to be returned by the DAO
        val mockSong = Song(
            id = 1L,
            album = AlbumDto(
                cover = "https://example.com/cover.jpg",
                cover_big = "https://example.com/cover_big.jpg",
                cover_medium = "https://example.com/cover_medium.jpg",
                cover_small = "https://example.com/cover_small.jpg",
                cover_xl = "https://example.com/cover_xl.jpg",
                albumId = 101,
                albumImage = "md5hashstringofimage",
                albumTitle = "Example Album",
                albumTrackList = "https://example.com/tracklist",
                albumType = "album"
            ),
            artist = ArtistDto(
                artistId = 201,
                artistLink = "https://example.com/artist",
                name = "Example Artist",
                picture = "https://example.com/artist_picture.jpg",
                picture_big = "https://example.com/artist_picture_big.jpg",
                picture_medium = "https://example.com/artist_picture_medium.jpg",
                picture_small = "https://example.com/artist_picture_small.jpg",
                picture_xl = "https://example.com/artist_picture_xl.jpg",
                tracklist = "https://example.com/artist_tracks",
                artistType = "artist"
            ),
            duration = 210,
            explicit_content_cover = 0,
            explicit_content_lyrics = 0,
            explicit_lyrics = false,
            link = "https://example.com/song",
            md5_image = "md5hashofsongimage",
            preview = "https://example.com/song_preview.mp3",
            rank = 5,
            readable = true,
            title = "Example Song",
            title_short = "Example",
            title_version = "Live",
            type = "track",
            genre = 1
        )

        `when`(songDatabase.songDao).thenReturn(songDao)
        `when`(songDao.getSongListByGenre(any())).thenReturn(listOf(mockSong))

        val repository = SongListRepositoryImpl(songApi, songDatabase)
        val result = repository.getSongList(false, 1).toList()

        verify(songDao).getSongListByGenre(1)
        assert(result[1] is Resource.Success) // Assert that the operation was successful
    }

    // Test to verify remote data fetching when local database is empty
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getSongList fetches from remote when local is empty`() = runBlockingTest {
        `when`(songDatabase.songDao).thenReturn(songDao)
        `when`(songDao.getSongListByGenre(any())).thenReturn(emptyList())

        // Setup: Create a mocked SongDto instance to simulate API response
        val testSongDto = SongDto(
            id = 1L,
            album = AlbumDto(
                cover = "https://example.com/album_cover.jpg",
                cover_big = "https://example.com/album_cover_big.jpg",
                cover_medium = "https://example.com/album_cover_medium.jpg",
                cover_small = "https://example.com/album_cover_small.jpg",
                cover_xl = "https://example.com/album_cover_xl.jpg",
                albumId = 101,
                albumImage = "md5hashofalbumimage",
                albumTitle = "Greatest Hits",
                albumTrackList = "https://example.com/album_tracklist",
                albumType = "album"
            ),
            artist = null, // Simulate missing artist data
            duration = 210,
            explicit_content_cover = 0,
            explicit_content_lyrics = 0,
            explicit_lyrics = false,
            link = "https://example.com/song",
            md5_image = "md5hashofsongimage",
            preview = "https://example.com/song_preview.mp3",
            rank = 5,
            readable = true,
            title = "Hit Song",
            title_short = "Hit",
            title_version = "Original Mix",
            type = "track"
        )

        val songListDto = SongListDto(listOf(testSongDto)) // Wrap the SongDto list in SongListDto
        `when`(songApi.getSongList(any())).thenReturn(songListDto)

        val repository = SongListRepositoryImpl(songApi, songDatabase)
        val result = repository.getSongList(true, 1).toList()

        verify(songApi).getSongList(1) // Verify that the API was called correctly
        assert(result[1] is Resource.Success) // Assert that the operation was successful
    }
}
