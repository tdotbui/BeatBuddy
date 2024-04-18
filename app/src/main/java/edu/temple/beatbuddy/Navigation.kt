package edu.temple.beatbuddy

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.temple.beatbuddy.app.screen.HomeScreen
import edu.temple.beatbuddy.music.screen.MusicBrowseScreen
import edu.temple.beatbuddy.user_profile.screen.UserProfileScreen
import edu.temple.beatbuddy.user_auth.screen.SignInScreen
import edu.temple.beatbuddy.user_auth.screen.SignUpScreen

@Composable
fun Navigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = "home"
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
            UserProfileScreen(
                onSignOut = {
                    navController.navigate("sign_in") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                goToMusicList = {
                    navController.navigate("music") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable(route = "home") {
            HomeScreen()
        }
    }
}