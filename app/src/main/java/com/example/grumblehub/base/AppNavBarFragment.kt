package com.example.grumblehub.base

import LoginScreen
import SignupScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.grumblehub.R
import com.example.grumblehub.core.datastore.DataStoreManager
import com.example.grumblehub.features.grievance.data.GrievanceViewModel
import com.example.grumblehub.features.grievance.ui.GrievanceScreen
import com.example.grumblehub.features.home.ui.HomeScreen
import com.example.grumblehub.features.login.data.AuthState
import com.example.grumblehub.features.login.ui.LoginOtpScreen
import com.example.grumblehub.features.onboarding.OnboardingScreen
import com.example.grumblehub.features.profile.ProfileScreen
import com.example.grumblehub.features.search.SearchScreen
import com.example.grumblehub.features.signup.ui.NewPasswordScreen
import com.example.grumblehub.features.signup.ui.SignupOtpScreen
import com.example.grumblehub.sharedviewmodels.GrievanceSharedViewModel
import org.koin.androidx.compose.koinViewModel

enum class AppNavHost {
    Onboarding,
    Login,
    Signup,
    SignupOtp,
    LoginOtp,
    Home,
    Profile,
    Search,
    Grievance,
    NewPassword
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

enum class Destination(
    val route: String,
    val label: String,
    val icon: Int,
    val contentDescription: String
) {
    PERSONAL("Personal", "Personal", R.drawable.baseline_password_24, "Personal"),
    GRUMBLERS("Grumblers", "Grumblers", R.drawable.baseline_search_24, "Grumblers"),
}


// Solution 1: Loading Screen Approach (Recommended)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppWithConditionalBottomBar(
    navController: NavHostController,
    authState: AuthState,
    dataStoreManager: DataStoreManager,
    onAuthStateChange: (AuthState) -> Unit
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinationRoute = currentBackStackEntry?.destination?.route

    val bottomBarRoutes = remember {
        setOf(
            AppNavHost.Home.name,
            AppNavHost.Profile.name,
            AppNavHost.Search.name
        )
    }

    // Show loading screen while auth state is being determined
    if (authState == AuthState.Loading) {
        LoadingScreen()
        return
    }

    val showBottomBar = authState == AuthState.Authenticated &&
            currentDestinationRoute in bottomBarRoutes
    val selectedDestination = if (showBottomBar) {
        AppNavBarDestination.Companion.entries.indexOfFirst {
            it.route == currentDestinationRoute
        }.takeIf { it >= 0 } ?: 0
    } else 0

    val startDestination = when (authState) {
        AuthState.Authenticated -> AppNavHost.Home.name
        AuthState.Unauthenticated -> AppNavHost.Onboarding.name
        else -> AppNavHost.Onboarding.name // Fallback
    }


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
        UnifiedNavHost(
            modifier = Modifier.padding(contentPadding),
            navController = navController,
            dataStoreManager = dataStoreManager,
            startDestination = startDestination,
            onLoginSuccess = { onAuthStateChange(AuthState.Authenticated) },
            onLogout = { onAuthStateChange(AuthState.Unauthenticated) }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoadingIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "GrumbleHub",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UnifiedNavHost(
    modifier: Modifier,
    navController: NavHostController,
    dataStoreManager: DataStoreManager,
    startDestination: String,
    onLoginSuccess: () -> Unit,
    onLogout: () -> Unit
) {

    // Shared View Models
    val grievanceSharedViewModel = viewModel<GrievanceSharedViewModel>()


    NavHost(
        navController = navController,
        startDestination = startDestination, // Use the determined start destination
        modifier = modifier
    ) {
        // Rest of your composable destinations remain the same...
        composable(AppNavHost.Onboarding.name) {
            OnboardingScreen(
                dataStoreManager = dataStoreManager,
                modifier = Modifier,
                navController = navController,
                onLoginSuccess = onLoginSuccess
            )
        }

        composable(AppNavHost.Signup.name) {
            SignupScreen(modifier = Modifier, navController = navController)
        }

        composable(AppNavHost.Login.name) {
            LoginScreen(modifier = Modifier, navController = navController)
        }

        composable("${AppNavHost.SignupOtp.name}/{email}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("email")
            if (userEmail != null) {
                SignupOtpScreen(
                    modifier = Modifier,
                    navController = navController,
                    email = userEmail
                )
            }
        }

        composable("${AppNavHost.LoginOtp.name}/{email}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("email")
            if (userEmail != null) {
                LoginOtpScreen(
                    modifier = Modifier,
                    navController = navController,
                    email = userEmail,
                    dataStoreManager = dataStoreManager
                )
            }
        }

        composable("${AppNavHost.NewPassword.name}/{email}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("email")
            if (userEmail != null) {
                NewPasswordScreen(
                    modifier = Modifier,
                    navController = navController,
                    email = userEmail,
                    dataStoreManager = dataStoreManager
                )
            }
        }

        composable(AppNavHost.Home.name) {
            HomeScreen(
                dataStoreManager = dataStoreManager,
                navController = navController,
                grievanceSharedViewModel = grievanceSharedViewModel,
                modifier = Modifier
            )
        }

        composable(AppNavHost.Profile.name) {
            ProfileScreen(
                modifier = Modifier,
                navController = navController
            )
        }

        composable(AppNavHost.Search.name) {
            SearchScreen(
                navController = navController,
                grievanceSharedViewModel = grievanceSharedViewModel,
                modifier = Modifier
            )
        }

        composable(AppNavHost.Grievance.name) {
            GrievanceScreen(
                modifier = Modifier,
                navController = navController,
                grievanceSharedViewModel = grievanceSharedViewModel
            )
        }
    }
}

// ============================================================
// Solution 2: Conditional NavHost Approach (Alternative)
// ============================================================

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppWithConditionalBottomBarAlt(
    navController: NavHostController,
    authState: AuthState,
    dataStoreManager: DataStoreManager,
    onAuthStateChange: (AuthState) -> Unit
) {
    when (authState) {
        AuthState.Loading -> {
            LoadingScreen()
        }
        AuthState.Authenticated -> {
            AuthenticatedApp(
                navController = navController,
                dataStoreManager = dataStoreManager,
                onLogout = { onAuthStateChange(AuthState.Unauthenticated) }
            )
        }
        AuthState.Unauthenticated -> {
            UnauthenticatedApp(
                navController = navController,
                dataStoreManager = dataStoreManager,
                onLoginSuccess = { onAuthStateChange(AuthState.Authenticated) }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthenticatedApp(
    navController: NavHostController,
    dataStoreManager: DataStoreManager,
    onLogout: () -> Unit
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinationRoute = currentBackStackEntry?.destination?.route
    val grievanceSharedViewModel = viewModel<GrievanceSharedViewModel>()

    val bottomBarRoutes = remember {
        setOf(
            AppNavHost.Home.name,
            AppNavHost.Profile.name,
            AppNavHost.Search.name
        )
    }

    val showBottomBar = currentDestinationRoute in bottomBarRoutes
    val selectedDestination = if (showBottomBar) {
        AppNavBarDestination.Companion.entries.indexOfFirst {
            it.route == currentDestinationRoute
        }.takeIf { it >= 0 } ?: 0
    } else 0

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
        NavHost(
            navController = navController,
            startDestination = AppNavHost.Home.name,
            modifier = Modifier.padding(contentPadding)
        ) {
            composable(AppNavHost.Home.name) {
                HomeScreen(
                    dataStoreManager = dataStoreManager,
                    navController = navController,
                    grievanceSharedViewModel = grievanceSharedViewModel,
                    modifier = Modifier
                )
            }

            composable(AppNavHost.Profile.name) {
                ProfileScreen(
                    modifier = Modifier,
                    navController = navController
                )
            }

            composable(AppNavHost.Search.name) {
                SearchScreen(
                    navController = navController,
                    grievanceSharedViewModel = grievanceSharedViewModel,
                    modifier = Modifier
                )
            }

            composable(AppNavHost.Grievance.name) {
                GrievanceScreen(
                    modifier = Modifier,
                    navController = navController,
                    grievanceSharedViewModel = grievanceSharedViewModel
                )
            }
        }
    }
}

@Composable
fun UnauthenticatedApp(
    navController: NavHostController,
    dataStoreManager: DataStoreManager,
    onLoginSuccess: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AppNavHost.Onboarding.name
    ) {
        composable(AppNavHost.Onboarding.name) {
            OnboardingScreen(
                dataStoreManager = dataStoreManager,
                modifier = Modifier,
                navController = navController,
                onLoginSuccess = onLoginSuccess
            )
        }

        composable(AppNavHost.Signup.name) {
            SignupScreen(modifier = Modifier, navController = navController)
        }

        composable(AppNavHost.Login.name) {
            LoginScreen(modifier = Modifier, navController = navController)
        }

        composable("${AppNavHost.SignupOtp.name}/{email}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("email")
            if (userEmail != null) {
                SignupOtpScreen(
                    modifier = Modifier,
                    navController = navController,
                    email = userEmail
                )
            }
        }

        composable("${AppNavHost.LoginOtp.name}/{email}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("email")
            if (userEmail != null) {
                LoginOtpScreen(
                    modifier = Modifier,
                    navController = navController,
                    email = userEmail,
                    dataStoreManager = dataStoreManager
                )
            }
        }

        composable("${AppNavHost.NewPassword.name}/{email}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("email")
            if (userEmail != null) {
                NewPasswordScreen(
                    modifier = Modifier,
                    navController = navController,
                    email = userEmail,
                    dataStoreManager = dataStoreManager
                )
            }
        }
    }
}

@Composable
fun OptimizedNavigationBar(
    navController: NavHostController,
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