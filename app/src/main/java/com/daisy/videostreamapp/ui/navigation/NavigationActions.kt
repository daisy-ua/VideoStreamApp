package com.daisy.videostreamapp.ui.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

class NavigationActions(navController: NavHostController) {

    val navigateToVideoScreen =
        { url: String, from: NavBackStackEntry ->
            if (from.lifecycleIsResumed()) {
                navController.navigate("${AppDestination.VIDEO_LIST_ROUTE.name}/$url")
            }
        }

    val navigateUp: (from: NavBackStackEntry) -> Unit = { from ->
        if (from.lifecycleIsResumed()) {
            navController.navigateUp()
        }
    }
}


private fun NavBackStackEntry.lifecycleIsResumed() =
    this.getLifecycle().currentState == Lifecycle.State.RESUMED