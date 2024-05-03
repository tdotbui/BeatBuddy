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

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: SongPostRepository
    private lateinit var viewModel: SwipeSongViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        kotlinx.coroutines.Dispatchers.setMain(testDispatcher)
        viewModel = SwipeSongViewModel(repository)
    }

    @Test
    fun testFetchSwipeSongPosts() = runTest {
        viewModel.fetchSwipeSongPosts()
        Mockito.verify(repository).fetchPostsFromFollowing()
    }

    @Test
    fun testRemoveSongFromList() = runTest {
        val songPost = SongPost()
        viewModel.setCurrentSong(songPost)
        viewModel.removeSongFromList()
        Mockito.verify(repository).deletePostFromFollowing(songPost)
    }

    @Test
    fun testLikePost() = runTest {
        val songPost = SongPost()
        viewModel.likePost(songPost)
        Mockito.verify(repository).likePost(songPost)
    }

    @After
    fun tearDown() {
        kotlinx.coroutines.Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }
}
