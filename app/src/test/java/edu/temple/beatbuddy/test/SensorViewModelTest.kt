import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.temple.beatbuddy.music_swipe.sensor.Direction
import edu.temple.beatbuddy.music_swipe.sensor.MeasurableSensor
import edu.temple.beatbuddy.music_swipe.view_model.SensorViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SensorViewModelTest {

    // Rule that swaps the background executor used by the Architecture Components with a different one which executes each task synchronously
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    // Mocked Sensor object
    @Mock
    private lateinit var sensor: MeasurableSensor

    // SensorViewModel instance that we are testing
    private lateinit var viewModel: SensorViewModel

    // Set up method to initialize the mocked objects and the ViewModel
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = SensorViewModel(sensor)
    }

    // Test case for the startListening method of the ViewModel
    @Test
    fun testStartListening() = runBlockingTest {
        // Call the method to be tested
        viewModel.startListening()

        // Verify that the startListening method of the sensor was called
        Mockito.verify(sensor).startListening()

        // Assert that the sensor is active
        assert(viewModel.isSensorActive.value)
    }

    // Test case for the stopListening method of the ViewModel
    @Test
    fun testStopListening() = runBlockingTest {
        // Call the method to be tested
        viewModel.stopListening()

        // Verify that the stopListening method of the sensor was called
        Mockito.verify(sensor).stopListening()

        // Assert that the sensor is not active
        assert(!viewModel.isSensorActive.value)
    }

    // Test case for the resetDirection method of the ViewModel
    @Test
    fun testResetDirection() = runBlockingTest {
        // Call the method to be tested
        viewModel.resetDirection()

        // Assert that the direction is NONE
        assert(viewModel.direction.value == Direction.NONE)
    }
}
