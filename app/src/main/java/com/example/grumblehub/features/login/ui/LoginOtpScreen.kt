package com.example.grumblehub.features.login.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.grumblehub.R
import com.example.grumblehub.base.AppNavHost
import com.example.grumblehub.core.datastore.DataStoreManager
import com.example.grumblehub.features.login.data.LoginViewModel
import com.example.grumblehub.features.login.data.VerifyLoginOtpRequest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoginOtpScreen(
    modifier: Modifier = Modifier,
    email: String,
    navController: NavHostController,
    dataStoreManager: DataStoreManager
) {
    val loginViewModel: LoginViewModel = koinViewModel()
    val otpStateObservable by loginViewModel.otpState.observeAsState(LoginViewModel.LoginUiState.Idle)
    val otpState = rememberTextFieldState()

    val otpMessageState = remember { mutableStateOf("") }
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }

    // Handle navigation events
    LaunchedEffect(Unit) {

        loginViewModel.loginEvents.collect { event ->

            when (event) {
                is LoginViewModel.LoginUiEvent.Navigate -> {
                    navController.navigate(AppNavHost.Home.name)
                }
            }
        }
    }

    // Handle login state changes and token saving
    LaunchedEffect(otpStateObservable) {
        when (val currentState = otpStateObservable) {
            is LoginViewModel.LoginUiState.Loading -> {
                buttonState = ButtonState.Loading
                otpMessageState.value = ""
            }

            is LoginViewModel.LoginUiState.Success -> {

                val response = currentState.response
                buttonState = ButtonState.Success
                otpMessageState.value = ""

                // Save token if it exists
                response.token?.let { token ->
                    try {
                        Log.d("response after otp login", response.toString())
                        dataStoreManager.saveJwtTokenSecure(token)
                        response.authUserResponse?.let {
                            dataStoreManager.setEmail(it.email)
                            dataStoreManager.setUserId(it.userId)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } ?: run {
                }
            }

            is LoginViewModel.LoginUiState.Error -> {
                val error = currentState.error
                otpMessageState.value = error.error
                buttonState = ButtonState.Error

                buttonState = ButtonState.Idle
            }


            LoginViewModel.LoginUiState.Idle -> {
                buttonState = ButtonState.Idle
                otpMessageState.value = ""
            }
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.grumble_hub),
                    contentDescription = "Grumble Hub Logo"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "OTP has been sent to your email. Use it within 5 minutes to verify your account.",
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                BasicTextField(
                    modifier = Modifier.semantics { contentType = ContentType.SmsOtpCode },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    state = otpState,
                    inputTransformation = InputTransformation.maxLength(6),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    decorator = {
                        val otpCode = otpState.text.toString()
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            repeat(6) { index ->
                                Digit(otpCode.getOrElse(index) { ' ' })
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(50.dp))

                if (otpMessageState.value.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = otpMessageState.value,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Box(contentAlignment = Alignment.Center) {
                Button(
                    enabled = otpState.text.isNotEmpty() && buttonState == ButtonState.Idle && otpState.text.length == 6,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    onClick = {
                        val request = VerifyLoginOtpRequest(
                            email = email,
                            otp = otpState.text.toString()
                        )
                        loginViewModel.verifyLoginOtp(request)
                    }
                ) {
                    Text("Submit", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                }

                when (buttonState) {
                    ButtonState.Loading -> {
                        LoadingIndicator()
                    }

                    else -> {} // Idle
                }
            }

            // Debug text - remove in production
            if (otpState.text.isNotEmpty()) {
                Text(
                    text = "OTP: ${otpState.text}"
                )
            }

        }
    }
}

@Composable
fun Digit(number: Char) {
    Box(
        Modifier
            .size(60.dp)
            .border(1.5.dp, Color.Transparent, RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(10.dp))
    ) {
        Text(
            text = number.toString(),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}