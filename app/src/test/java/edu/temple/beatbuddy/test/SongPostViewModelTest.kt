package edu.temple.beatbuddy.test

import edu.temple.beatbuddy.music_post.model.MockPost
import edu.temple.beatbuddy.music_post.view_model.SongPostViewModel
import edu.temple.beatbuddy.music_swipe.view_model.SwipeSongViewModel
import edu.temple.beatbuddy.repository.TestSongPostRepository
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SongPostViewModelTest {

    private lateinit var viewModel: SongPostViewModel
    private lateinit var testSongPostRepository: TestSongPostRepository

    @Before
    fun setUp() {
        testSongPostRepository = TestSongPostRepository()
        Dispatchers.setMain(TestCoroutineDispatcher())

        viewModel = SongPostViewModel(
            repository = testSongPostRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test fetchSongPosts`() = runBlocking {
        val expectedPosts = MockPost.posts
        testSongPostRepository.fetchAllPostsFromFirestore().collect { result ->
            Assert.assertEquals(expectedPosts, result.data)
        }
    }

    @Test
    fun `test fetchPostForUser`() = runBlocking {
        val user = User(id = MockPost.posts[0].ownerUid)
        testSongPostRepository.fetchPostsForUser(user).collect {result ->
            Assert.assertEquals(result.data?.size, 2)
        }
    }

    @Test
    fun `test likePost`() = runBlocking {
        val result = testSongPostRepository.likePost(MockPost.posts[0])
        Assert.assertEquals(Resource.Success(true).data, result.data)
    }

    @Test
    fun `test unlikePost`() = runBlocking {
        val result = testSongPostRepository.likePost(MockPost.posts[0])
        Assert.assertEquals(Resource.Success(true).data, result.data)
    }

    @Test
    fun `test makePost`() = runBlocking {
        val result = testSongPostRepository.shareAPost(MockPost.posts[2])
        Assert.assertEquals(Resource.Success(true).data, result.data)
    }

    @Test
    fun `test deletePost`() = runBlocking {
        val result = testSongPostRepository.deleteAPost(MockPost.posts[2])
        Assert.assertEquals(Resource.Success(true).data, result.data)
    }
}