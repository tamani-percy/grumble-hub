package com.example.grumblehub.features.core

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.grumblehub.R
import com.example.grumblehub.core.datastore.DataStoreManager
import com.example.grumblehub.features.grievance.ui.GrievanceScreen
import com.example.grumblehub.features.home.ui.HomeScreen
import com.example.grumblehub.features.konfetti.KonfettiViewModel
import com.example.grumblehub.features.settings.SettingsScreen
import com.example.grumblehub.features.search.SearchScreen
import com.example.grumblehub.features.timeline.ui.TimelineScreen


const val ARG_GRIEVANCE_ID = "grievanceId"

enum class AppNavHost {
    Home,
    Timeline,
    Settings,
    Grievance,
    Search
}

enum class AppNavBarDestination(
    val iconResId: Int,
    val label: String,
    val contentDescription: String,
    val route: String,

    ) {
    HOME(
        iconResId = R.drawable.baseline_home_24,
        label = "Home",
        contentDescription = "Navigate to Home Screen",
        route = AppNavHost.Home.name,
    ),
    TIMELINE(
        iconResId = R.drawable.baseline_timeline_24,
        label = "Timeline",
        contentDescription = "Navigate to Timeline Screen",
        route = AppNavHost.Timeline.name,
    ),
    SEARCH(
        iconResId = R.drawable.baseline_search_24,
        label = "Search",
        contentDescription = "Navigate to Search Screen",
        route = AppNavHost.Search.name,
    );

    companion object {
        val entries = listOf(HOME, TIMELINE, SEARCH)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavBarGraph(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppNavHost.Home.name,
    dataStoreManager: DataStoreManager
) {
    val context = LocalContext.current
    val konfettiViewModel = viewModel<KonfettiViewModel>()
    val konfettiState: KonfettiViewModel.State by konfettiViewModel.state.observeAsState(
        KonfettiViewModel.State.Idle,
    )
    val heartDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_heart)

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300) // Reduced from 650ms
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300) // Reduced from 650ms
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300) // Reduced from 650ms
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300) // Reduced from 650ms
            )
        }
    ) {


        composable(
            route = AppNavHost.Home.name
        ) {
            HomeScreen(
                modifier = Modifier,
                navController = navController,
                dataStoreManager = dataStoreManager,
                konfettiState = konfettiState,
                context = context,
                heartDrawable = heartDrawable,
                konfettiViewModel = konfettiViewModel
            )
        }

        composable(
            route = AppNavHost.Timeline.name
        ) {
            TimelineScreen()
        }

        composable(
            route = AppNavHost.Search.name
        ) {
            SearchScreen(modifier = modifier, navController = navController)
        }

        composable(
            route = AppNavHost.Settings.name
        ) {
            SettingsScreen(modifier = modifier, navController = navController)
        }
        composable(
            route = "${AppNavHost.Grievance.name}/{$ARG_GRIEVANCE_ID}",
            arguments = listOf(
                navArgument(ARG_GRIEVANCE_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val grievanceId = backStackEntry.arguments!!.getLong(ARG_GRIEVANCE_ID)
            GrievanceScreen(
                context = context,
                modifier = modifier,
                navController = navController,
                grievanceId = grievanceId,
                onNavigateToHome = {
                    navController.navigate(AppNavHost.Home.name)
                }
            )
        }

    }
}