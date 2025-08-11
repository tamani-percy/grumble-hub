package com.example.grumblehub.base

import LoginScreen
import SignupScreen
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.grumblehub.core.datastore.DataStoreManager
import com.example.grumblehub.features.home.ui.HomeScreen
import com.example.grumblehub.features.login.data.AuthState
import com.example.grumblehub.features.login.ui.LoginOtpScreen
import com.example.grumblehub.features.onboarding.OnboardingScreen
import com.example.grumblehub.features.profile.ProfileScreen
import com.example.grumblehub.features.search.SearchScreen
import com.example.grumblehub.features.signup.ui.NewPasswordScreen
import com.example.grumblehub.features.signup.ui.SignupOtpScreen

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppEntryPoint(modifier: Modifier, dataStoreManager: DataStoreManager) {
    var authState by remember { mutableStateOf<AuthState>(AuthState.Loading) }
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        authState = if (dataStoreManager.isUserAuthenticated()) {
            AuthState.Authenticated
        } else {
            AuthState.Unauthenticated
        }
    }

    when (authState) {
        AuthState.Loading -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                LoadingIndicator()
            }
        }

        AuthState.Authenticated, AuthState.Unauthenticated -> {
            // Use single app with conditional bottom bar
            AppWithConditionalBottomBar(
                navController = navController,
                authState = authState,
                dataStoreManager = dataStoreManager,
                onAuthStateChange = { newState -> authState = newState }
            )
        }
    }
}


