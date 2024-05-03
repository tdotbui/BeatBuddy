import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.repository.SongPostRepository
import edu.temple.beatbuddy.music_swipe.view_model.SwipeSongViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SwipeSongViewModelTest {

    // Test dispatcher for running tests
    private val testDispatcher = StandardTestDispatcher()

    // Rule that swaps the background executor used by the Architecture Components with a different one which executes each task synchronously
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    // Mock of SongPostRepository
    @Mock
    private lateinit var repository: SongPostRepository

    // ViewModel that is tested
    private lateinit var viewModel: SwipeSongViewModel

    // Set up the environment for testing
    @Before
    fun setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this)
        // Set main dispatcher to the test dispatcher
        kotlinx.coroutines.Dispatchers.setMain(testDispatcher)
        // Initialize ViewModel
        viewModel = SwipeSongViewModel(repository)
    }

    // Test the fetchSwipeSongPosts function
    @Test
    fun testFetchSwipeSongPosts() = runTest {
        // Call the function
        viewModel.fetchSwipeSongPosts()
        // Verify that fetchPostsFromFollowing was called
        Mockito.verify(repository).fetchPostsFromFollowing()
    }

    // Test the removeSongFromList function
    @Test
    fun testRemoveSongFromList() = runTest {
        // Create a SongPost
        val songPost = SongPost()
        // Set the current song
        viewModel.setCurrentSong(songPost)
        // Call the function
        viewModel.removeSongFromList()
        // Verify that deletePostFromFollowing was called with the correct argument
        Mockito.verify(repository).deletePostFromFollowing(songPost)
    }

    // Test the likePost function
    @Test
    fun testLikePost() = runTest {
        // Create a SongPost
        val songPost = SongPost()
        // Call the function
        viewModel.likePost(songPost)
        // Verify that likePost was called with the correct argument
        Mockito.verify(repository).likePost(songPost)
    }

    // Clean up after testing
    @After
    fun tearDown() {
        // Reset main dispatcher to the original Main dispatcher
        kotlinx.coroutines.Dispatchers.resetMain()
    }
}
