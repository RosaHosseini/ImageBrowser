package com.rosahosseini.findr.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.rosahosseini.findr.bookmark.navigation.bookmarkGraph
import com.rosahosseini.findr.navigation.Navigator
import com.rosahosseini.findr.navigation.destinations.SearchDestination
import com.rosahosseini.findr.navigation.extensions.navigate
import com.rosahosseini.findr.photodetail.navigation.photoDetailGraph
import com.rosahosseini.findr.search.navigation.searchGraph
import kotlinx.coroutines.flow.filterNotNull

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun FindrNaveHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = SearchDestination.route,
    navigator: Navigator
) {
    LaunchedEffect(Navigator.KEY) {
        navigator.navigationActionFlow.filterNotNull().collect {
            navController.navigate(it)
        }
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        searchGraph()
        photoDetailGraph(navController)
        bookmarkGraph()
    }
}