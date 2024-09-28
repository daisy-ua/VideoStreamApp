package com.daisy.videostreamapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daisy.videostreamapp.ui.navigation.AppDestination.*
import com.daisy.videostreamapp.ui.screen.VideoListScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = VIDEO_LIST_ROUTE.name
) {
    val actions = remember(navController) { NavigationActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(VIDEO_LIST_ROUTE.name) { navBackStackEntry ->
            VideoListScreen()
        }
    }
}