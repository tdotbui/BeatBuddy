import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.temple.beatbuddy.music_post.model.MockPost
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.repository.SongPostRepository
import edu.temple.beatbuddy.music_swipe.view_model.SwipeSongViewModel
import edu.temple.beatbuddy.repository.TestAuthRepository
import edu.temple.beatbuddy.repository.TestFollowRepository
import edu.temple.beatbuddy.repository.TestSongPostRepository
import edu.temple.beatbuddy.repository.TestUserRepository
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.user_profile.view_model.CurrentUserProfileViewModel
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SwipeSongViewModelTest {

    private lateinit var viewModel: SwipeSongViewModel
    private lateinit var testSongPostRepository: TestSongPostRepository

    @Before
    fun setUp() {
        testSongPostRepository = TestSongPostRepository()
        Dispatchers.setMain(TestCoroutineDispatcher())

        viewModel = SwipeSongViewModel(
            repository = testSongPostRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test fetchPostsFromFollowing`() = runBlocking {
        val expectedPosts = MockPost.posts.filter { it.didLike!! }
        testSongPostRepository.fetchPostsFromFollowing().collect { result ->
            assertEquals(expectedPosts, result.data)
        }
    }

    @Test
    fun `test likePost`() = runBlocking {
        val result = testSongPostRepository.likePost(MockPost.posts[0])
        assertEquals(Resource.Success(true).data, result.data)
    }
}
