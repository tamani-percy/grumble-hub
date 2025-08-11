package com.example.grumblehub.features.signup.ui

import ButtonState
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
import com.example.grumblehub.features.signup.data.SignupViewModel
import com.example.grumblehub.features.signup.data.VerifySignupOtpRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SignupOtpScreen(
    modifier: Modifier = Modifier,
    email: String,
    navController: NavHostController,
) {
    val signupViewModel:SignupViewModel = koinViewModel()

    val signupMessageState = remember { mutableStateOf("") }
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }

    val signupState by signupViewModel.signupState.observeAsState(SignupViewModel.SignupUiState.Idle)
    val otpState = rememberTextFieldState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        signupViewModel.signupEvents.collect { event ->
            when (event) {
                is SignupViewModel.SignupEvent.NavigateToDashboard -> {
                    navController.navigate("${AppNavHost.NewPassword.name}/${email}")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        when (signupState) {
            is SignupViewModel.SignupUiState.Error -> {
                val error = (signupState as SignupViewModel.SignupUiState.Error).error
                signupMessageState.value = error.error
                buttonState = ButtonState.Error
            }

            is SignupViewModel.SignupUiState.Success -> {
                signupMessageState.value = ""
                buttonState = ButtonState.Success
            }

            is SignupViewModel.SignupUiState.Loading -> {
                buttonState = ButtonState.Loading
            }

            SignupViewModel.SignupUiState.Idle -> {
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

            if (signupMessageState.value.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = signupMessageState.value,
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
                    coroutineScope.launch {
                        buttonState = ButtonState.Loading
                        delay(5000)
                        signupViewModel.verifySignupOtp(
                            VerifySignupOtpRequest(
                                email = email,
                                otp = otpState.text.toString()
                            )
                        )
                    }
                }
            ) {
                Text("Submit", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            }

            when (buttonState) {
                ButtonState.Loading -> LoadingIndicator()
                else -> {} // Idle
            }
        }
        if (otpState.text.isNotEmpty()) {
            Text(
                text = otpState.text.toString()
            )
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