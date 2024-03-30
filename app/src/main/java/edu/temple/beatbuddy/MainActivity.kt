package edu.temple.beatbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import edu.temple.beatbuddy.repository.auth.AuthViewModel
import edu.temple.beatbuddy.ui.theme.BeatBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeatBuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel : AuthViewModel = viewModel()

                    MyApp(
                        navController = navController,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MyApp(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    Navigation(
        navController = navController,
        authViewModel = authViewModel
    )
}
