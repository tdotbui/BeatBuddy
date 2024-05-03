package edu.temple.beatbuddy.test

import com.google.firebase.auth.FirebaseUser
import edu.temple.beatbuddy.repository.TestAuthRepository
import edu.temple.beatbuddy.repository.TestFollowRepository
import edu.temple.beatbuddy.repository.TestUserRepository
import edu.temple.beatbuddy.user_discover.repository.UsersRepository
import edu.temple.beatbuddy.user_profile.view_model.CurrentUserProfileViewModel
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    fun testFirebaseSignUpWithEmailAndPassword() = runBlocking {
        val authRepository = TestAuthRepository(mockFirebaseUser)
        val result = authRepository.firebaseSignUpWithEmailAndPassword("test@example.com", "password", "John Doe", "john_doe")
        assertEquals(Resource.Success(true).data, result.data)
    }
}