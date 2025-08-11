package com.example.grumblehub.features.signup.ui

import ButtonState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextObfuscationMode.Companion.RevealLastTyped
import androidx.compose.foundation.text.input.TextObfuscationMode.Companion.Visible
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.grumblehub.R
import com.example.grumblehub.base.AppNavHost
import com.example.grumblehub.core.datastore.DataStoreManager
import com.example.grumblehub.features.signup.data.CreatePasswordRequest
import com.example.grumblehub.features.signup.data.SignupViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewPasswordScreen(
    modifier: Modifier = Modifier,
    email: String,
    navController: NavHostController,
    dataStoreManager: DataStoreManager
) {

    val signupViewModel: SignupViewModel = koinViewModel()

    val signupMessageState = remember { mutableStateOf("") }
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scope = rememberCoroutineScope()

    val newPasswordState by signupViewModel.newPasswordState.observeAsState(SignupViewModel.NewPasswordUiState.Idle)
    val passwordState = rememberTextFieldState()
    val confirmPasswordState = rememberTextFieldState()

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val passwordsMatch = passwordState.text == confirmPasswordState.text
    val showError =
        passwordState.text.isNotEmpty() && confirmPasswordState.text.isNotEmpty() && !passwordsMatch

    LaunchedEffect(Unit) {
        signupViewModel.signupEvents.collect { event ->
            when (event) {
                is SignupViewModel.SignupEvent.NavigateToDashboard -> {
                    navController.navigate(AppNavHost.Home.name)
                }
            }
        }
    }


    LaunchedEffect(newPasswordState) {
        when (newPasswordState) {
            is SignupViewModel.NewPasswordUiState.Error -> {
                val error = (newPasswordState as SignupViewModel.NewPasswordUiState.Error).error
                signupMessageState.value = error.error
                buttonState = ButtonState.Error
            }

            is SignupViewModel.NewPasswordUiState.Success -> {
                val response =
                    (newPasswordState as SignupViewModel.NewPasswordUiState.Success).response
                signupMessageState.value = ""
                buttonState = ButtonState.Success
                print("Token is: $response")
                response.token.let { token ->
                    scope.launch {
                        dataStoreManager.saveJwtTokenSecure(token)
                    }
                }
            }

            is SignupViewModel.NewPasswordUiState.Loading -> {
                buttonState = ButtonState.Loading
            }

            SignupViewModel.NewPasswordUiState.Idle -> {
                signupMessageState.value = ""
                buttonState = ButtonState.Idle
            }
        }
    }


    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(R.drawable.grumble_hub),
                contentDescription = "Grumble Hub Logo"
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = "Please enter a new password")

            Spacer(modifier = Modifier.height(50.dp))

            OutlinedSecureTextField(
                modifier = Modifier.semantics { contentType = ContentType.Password },
                state = passwordState,
                shape = RoundedCornerShape(100.dp),
                placeholder = { Text("Enter new password") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_password_24),
                        contentDescription = "Lock Icon",
                        modifier = Modifier.width(24.dp)
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = if (passwordVisible)
                            painterResource(R.drawable.baseline_visibility_24)
                        else
                            painterResource(R.drawable.baseline_visibility_off_24),
                        contentDescription = "Toggle Password Visibility",
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    )
                },
                textObfuscationMode = if (passwordVisible) Visible else RevealLastTyped
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedSecureTextField(
                modifier = Modifier.semantics { contentType = ContentType.Password },
                state = confirmPasswordState,
                shape = RoundedCornerShape(100.dp),
                placeholder = { Text("Confirm your password") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_password_24),
                        contentDescription = "Lock Icon",
                        modifier = Modifier.width(24.dp)
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = if (confirmPasswordVisible)
                            painterResource(R.drawable.baseline_visibility_24)
                        else
                            painterResource(R.drawable.baseline_visibility_off_24),
                        contentDescription = "Toggle Confirm Password Visibility",
                        modifier = Modifier.clickable {
                            confirmPasswordVisible = !confirmPasswordVisible
                        }
                    )
                },
                textObfuscationMode = if (confirmPasswordVisible) Visible else RevealLastTyped
            )

            if (showError) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Passwords do not match",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
        Box(contentAlignment = Alignment.Center) {

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                onClick = {
                    if (passwordsMatch) {
                        val response = signupViewModel.createPassword(
                            CreatePasswordRequest(
                                email = email,
                                password = passwordState.text.toString()
                            )
                        )
                    }
                },
                enabled = passwordsMatch && passwordState.text.isNotEmpty()
            ) {
                Text(text = "Log in", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            }

            when (buttonState) {
                ButtonState.Loading -> LoadingIndicator()
                else -> {} // Idle
            }
        }
    }
}
