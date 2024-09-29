package com.daisy.videostreamapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.daisy.videostreamapp.ui.navigation.AppDestination.VIDEO_LIST_ROUTE
import com.daisy.videostreamapp.ui.navigation.AppDestination.VIDEO_URL_KEY
import com.daisy.videostreamapp.ui.navigation.AppDestination.VIDEO_URL_ROUTE
import com.daisy.videostreamapp.ui.screen.VideoListScreen
import com.daisy.videostreamapp.ui.screen.VideoPlayerScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = VIDEO_LIST_ROUTE.name
) {
    val actions = remember(navController) { NavigationActions(navController) }

    val viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(VIDEO_LIST_ROUTE.name) { navBackStackEntry ->
            VideoListScreen(
                viewModel = hiltViewModel(viewModelStoreOwner),
                onItemClicked = { title -> actions.navigateToVideoScreen(title, navBackStackEntry) }
            )
        }

        composable(
            route = "${VIDEO_URL_ROUTE.name}/{${VIDEO_URL_KEY.name}}",
            arguments = listOf(
                navArgument(VIDEO_URL_KEY.name) {
                    type = NavType.IntType
                }
            ),
        ) { backStackEntry: NavBackStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val videoIndex = arguments.getInt(VIDEO_URL_KEY.name)

            VideoPlayerScreen(
                videoIndex = videoIndex,
                viewModel = hiltViewModel(viewModelStoreOwner),
                onUpClicked = { actions.navigateUp(backStackEntry) }
            )
        }
    }
}