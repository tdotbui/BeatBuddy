package edu.temple.beatbuddy

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import edu.temple.beatbuddy.app.screen.HomeScreen
import edu.temple.beatbuddy.user_auth.screen.SignInScreen
import edu.temple.beatbuddy.user_auth.screen.SignUpScreen

@Composable
fun Navigation(
    navController: NavHostController,
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val isUserSignedIn = (currentUser != null)

    var startDestination = "sign_in"

    if (isUserSignedIn) {
        startDestination = "home"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = "sign_in") {
            SignInScreen(
                goToSignUpScreen = {
                    navController.navigate("sign_up") {
                        popUpTo("sign_in") { inclusive = true }
                    } },
                onSignIn = { navController.navigate("home") {
                    popUpTo("sign_in") { inclusive = true }
                } }
            )
        }

        composable(route = "sign_up") {
            SignUpScreen(
                goToSignInScreen = {
                    navController.navigate("sign_in") {
                        popUpTo("sign_up") { inclusive = true }
                    }
                }
            )
        }

        composable(route = "home") {
            HomeScreen(
                goToSignInScreen = {
                    navController.navigate("sign_in") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}