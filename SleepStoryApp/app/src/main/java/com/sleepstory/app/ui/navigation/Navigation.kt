package com.sleepstory.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sleepstory.app.ui.screens.assessment.AssessmentScreen
import com.sleepstory.app.ui.screens.auth.LoginScreen
import com.sleepstory.app.ui.screens.auth.RegisterScreen
import com.sleepstory.app.ui.screens.discover.DiscoverScreen
import com.sleepstory.app.ui.screens.generate.GenerateScreen
import com.sleepstory.app.ui.screens.generating.GeneratingScreen
import com.sleepstory.app.ui.screens.home.HomeScreen
import com.sleepstory.app.ui.screens.player.PlayerScreen
import com.sleepstory.app.ui.screens.profile.ProfileScreen
import com.sleepstory.app.ui.screens.splash.SplashScreen
import com.sleepstory.app.ui.screens.welcome.WelcomeScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Assessment : Screen("assessment")
    object Home : Screen("home")
    object Discover : Screen("discover")
    object Generate : Screen("generate")
    object Generating : Screen("generating/{requestId}") {
        fun createRoute(requestId: String) = "generating/$requestId"
    }
    object Player : Screen("player/{storyId}") {
        fun createRoute(storyId: String) = "player/$storyId"
    }
    object Profile : Screen("profile")
}

@Composable
fun SleepStoryNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToWelcome = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onStartAssessment = {
                    navController.navigate(Screen.Assessment.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigateUp()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Assessment.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Assessment.route) {
            AssessmentScreen(
                onAssessmentComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Assessment.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController
            )
        }

        composable(Screen.Discover.route) {
            DiscoverScreen(
                navController = navController
            )
        }

        composable(Screen.Generate.route) {
            GenerateScreen(
                navController = navController
            )
        }

        composable(
            route = Screen.Generating.route,
            arguments = listOf(
                navArgument("requestId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments?.getString("requestId") ?: ""
            GeneratingScreen(
                requestId = requestId,
                navController = navController
            )
        }

        composable(
            route = Screen.Player.route,
            arguments = listOf(
                navArgument("storyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val storyId = backStackEntry.arguments?.getString("storyId") ?: ""
            PlayerScreen(
                storyId = storyId,
                navController = navController
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                navController = navController
            )
        }
    }
}
