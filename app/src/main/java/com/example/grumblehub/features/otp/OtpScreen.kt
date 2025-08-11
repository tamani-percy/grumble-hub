package com.example.grumblehub.features.otp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.grumblehub.R
import com.example.grumblehub.core.AppNavHost

@Composable
fun OtpScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val otpState = rememberTextFieldState()
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.grumble_hub),
            contentDescription = "Grumble Hub Logo"
        )

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
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,

                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            onClick = {
                navController.navigate(AppNavHost.Home.name)
            }) {
            Text(text = "Submit", fontSize = MaterialTheme.typography.titleLarge.fontSize)

        }
    }

}


@Composable
fun Digit(number: Char) {
    Box(
        Modifier
            .size(48.dp)
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