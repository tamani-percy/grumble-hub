package com.example.grumblehub.core

import LoginScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.grumblehub.R
import com.example.grumblehub.features.grievance.ui.GrievanceScreen
import com.example.grumblehub.features.home.ui.HomeScreen
import com.example.grumblehub.features.onboarding.OnboardingScreen
import com.example.grumblehub.features.otp.OtpScreen
import com.example.grumblehub.features.profile.ProfileScreen
import com.example.grumblehub.features.search.SearchScreen

enum class AppNavHost {
    Onboarding,
    Login,
    Signup,
    Otp,
    Home,
    Profile,
    Grievance,
    Search
}

enum class AppNavBarDestination(
    val iconResId: Int,
    val label: String,
    val contentDescription: String,
    val route: String,
    val shouldShowBadge: Boolean = false


) {
    HOME(
        iconResId = R.drawable.baseline_home_24,
        label = "Home",
        contentDescription = "Navigate to Home Screen",
        route = AppNavHost.Home.name,
        shouldShowBadge = true
    ),
    SEARCH(
        iconResId = R.drawable.baseline_search_24,
        label = "Search",
        contentDescription = "Navigate to Search Screen",
        route = AppNavHost.Search.name,
        shouldShowBadge = false
    ),
    PROFILE(
        iconResId = R.drawable.baseline_person_24,
        label = "Profile",
        contentDescription = "Navigate to Profile Screen",
        route = AppNavHost.Profile.name,
        shouldShowBadge = true,
    );

    companion object {
        val entries = listOf(HOME, SEARCH, PROFILE)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavBarGraph(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppNavHost.Onboarding.name
) {
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
            route = AppNavHost.Onboarding.name
        ) {
            OnboardingScreen(modifier = modifier, navController = navController)
        }

        composable(
            route = AppNavHost.Login.name
        ) {
            LoginScreen(modifier = modifier, navController = navController)
        }

        composable(
            route = AppNavHost.Otp.name
        ) {
            OtpScreen(modifier = modifier, navController = navController)
        }

        composable(
            route = AppNavHost.Home.name
        ) {
            HomeScreen(modifier = Modifier, navController = navController)
        }

        composable(
            route = AppNavHost.Search.name
        ) {
            SearchScreen(modifier = modifier, navController = navController)
        }

        composable(
            route = AppNavHost.Profile.name
        ) {
            ProfileScreen(modifier = modifier, navController = navController)
        }
        composable(
            route = AppNavHost.Grievance.name
        ) {
            GrievanceScreen(modifier = modifier, navController = navController)
        }

    }
}