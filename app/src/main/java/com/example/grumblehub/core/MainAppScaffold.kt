package com.example.grumblehub.core

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MainAppScaffold() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinationRoute = currentBackStackEntry?.destination?.route

    // Simplified bottom bar visibility logic
    val bottomBarRoutes = remember {
        setOf(
            AppNavHost.Home.name,
            AppNavHost.Profile.name,
            AppNavHost.Search.name
        )
    }

    val showBottomBar = currentDestinationRoute in bottomBarRoutes

    val selectedDestination = AppNavBarDestination.Companion.entries.indexOfFirst {
        it.route == currentDestinationRoute
    }.takeIf { it >= 0 } ?: 0

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                OptimizedNavigationBar(
                    navController = navController,
                    currentRoute = currentDestinationRoute,
                    selectedDestination = selectedDestination
                )
            }
        }
    ) { contentPadding ->
        AppNavBarGraph(
            modifier = Modifier.padding(contentPadding),
            navController = navController,
            startDestination = AppNavHost.Home.name
        )
    }
}

@Composable
private fun OptimizedNavigationBar(
    navController: androidx.navigation.NavHostController,
    currentRoute: String?,
    selectedDestination: Int
) {
    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
    ) {
        AppNavBarDestination.Companion.entries.forEach { destination ->
            val selected = currentRoute == destination.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    val current = navController.currentDestination?.route
                    if (current != destination.route) {
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    NavigationIcon(destination = destination, selected = selected)
                },
                label = {
                    NavigationLabel(destination = destination, selected = selected)
                },
            )
        }
    }
}

@Composable
private fun NavigationIcon(
    destination: AppNavBarDestination,
    selected: Boolean
) {
    val tint by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        },
        label = "iconColor"
    )

    BadgedBox(
        badge = {
            if (destination.shouldShowBadge) {
                Badge { Text("3") }
            }
        }
    ) {
        Icon(
            painter = painterResource(destination.iconResId),
            contentDescription = destination.contentDescription,
            tint = tint
        )
    }
}

@Composable
private fun NavigationLabel(
    destination: AppNavBarDestination,
    selected: Boolean
) {
    Text(
        text = destination.label,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
    )
}
