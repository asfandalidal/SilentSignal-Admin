package com.azeemi.adminannouncement.presentation.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen {
                navController.navigate("admin") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
        composable("admin") {
            AdminScreen()
        }
    }
}
