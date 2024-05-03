package edu.temple.beatbuddy.test

import com.google.firebase.auth.FirebaseUser
import edu.temple.beatbuddy.repository.TestAuthRepository
import edu.temple.beatbuddy.repository.TestFollowRepository
import edu.temple.beatbuddy.repository.TestUserRepository
import edu.temple.beatbuddy.user_auth.model.MockUser
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.user_auth.model.UserStats
import edu.temple.beatbuddy.user_discover.repository.UsersRepository
import edu.temple.beatbuddy.user_profile.view_model.CurrentUserProfileViewModel
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class UserVieModelTest {
    // this test includes SignIn and SignUp View Model
    private val mockFirebaseUser: FirebaseUser = mock(FirebaseUser::class.java)

    private lateinit var viewModel: CurrentUserProfileViewModel
    private lateinit var testUserRepository: TestUserRepository
    private lateinit var testAuthRepository: TestAuthRepository
    private lateinit var testFollowRepository: TestFollowRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        testUserRepository = TestUserRepository()
        testAuthRepository = TestAuthRepository(mockFirebaseUser)
        testFollowRepository = TestFollowRepository()

        Dispatchers.setMain(TestCoroutineDispatcher())

        viewModel = CurrentUserProfileViewModel(
            auth = testAuthRepository,
            repository = testFollowRepository,
            userRepo = testUserRepository
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test testFirebaseSignUpWithEmailAndPassword`() = runBlocking {
        val authRepository = TestAuthRepository(mockFirebaseUser)
        val result = testAuthRepository.firebaseSignUpWithEmailAndPassword("test@example.com", "password", "John Doe", "john_doe")
        assertEquals(Resource.Success(true).data, result.data)
    }

    @Test
    fun `test firebaseSignInWithEmailAndPassword`() = runBlocking {
        val email = "test@example.com"
        val password = "password"
        val result = testAuthRepository.firebaseSignInWithEmailAndPassword(email, password)
        assertEquals(Resource.Success(true).data, result.data)
    }

    @Test
    fun `test fetchCurrentUser`() = runBlocking {
        val expectedUser = User("Test User", "test@example.com")
        val actualUser = testAuthRepository.fetchCurrentUser().data
        assertEquals(expectedUser, actualUser)
    }

    @Test
    fun `test signOut`() {
        val result = testAuthRepository.signOut()
        assertEquals(Resource.Success(true).data, result.data)
    }

    @Test
    fun `test fetchCurrentUserStats`() = runBlocking {
        val userId = MockUser.users[2].id
        val userStats = MockUser.users[2].stats
        testFollowRepository.fetchUserStats(userId, true).collectLatest { result ->
            assertEquals(userStats, result.data)
        }
    }

    @Test
    fun`test updateUserProfile`() = runBlocking {
        MockUser.users[0].let {
            testUserRepository.updateProfile(
                username = it.username,
                imageUrl = it.profileImage!!,
                bio = "new bio",
                shouldUpdate = true
            ).let { result ->
                assertEquals(Resource.Success(true).data, result.data)
            }
        }
    }

    @Test
    fun `test fetchAllUsersFromFireStore`() = runBlocking {
        val expectedUsers = MockUser.users[0]
        testUserRepository.fetchAllUsersFromFireStore().collectLatest { result ->
            // test using the list size
            assertEquals(result.data?.size, MockUser.users.size)

            // test using the actual element
            val actualUsers = result.data?.get(0)
            assertEquals(expectedUsers, actualUsers)
        }
    }

    @Test
    fun `test follow`() = runBlocking {
        val result = testFollowRepository.follow("userId")
        assertEquals(Resource.Success(true).data, result.data)
    }

    @Test
    fun `test unfollow`() = runBlocking {
        val result = testFollowRepository.unfollow("userId")
        assertEquals(Resource.Success(true).data, result.data)
    }

    @Test
    fun `test checkIfUserIsFollowed`() = runBlocking {
        val result = testFollowRepository.checkIfUserIsFollowed("userId")
        assertEquals(Resource.Success(true).data, result.data)
    }
}