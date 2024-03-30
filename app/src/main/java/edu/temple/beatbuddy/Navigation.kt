package edu.temple.beatbuddy

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.temple.beatbuddy.repository.auth.AuthViewModel
import edu.temple.beatbuddy.screen.HomeScreen
import edu.temple.beatbuddy.screen.SignInScreen
import edu.temple.beatbuddy.screen.SignUpScreen

@Composable
fun Navigation(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "sign_in"
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
                authViewModel = authViewModel,
                goToSignInScreen = {
                    navController.navigate("sign_in") {
                        popUpTo("sign_up") { inclusive = true }
                    }
                }
            )
        }

        composable(route = "home") {
            HomeScreen(
                onSignOut = {
                    navController.navigate("sign_in") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}