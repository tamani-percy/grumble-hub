import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextObfuscationMode.Companion.RevealLastTyped
import androidx.compose.foundation.text.input.TextObfuscationMode.Companion.Visible
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.grumblehub.R
import com.example.grumblehub.base.AppNavHost
import com.example.grumblehub.features.login.data.LoginRequest
import com.example.grumblehub.features.login.data.LoginViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val loginViewModel:LoginViewModel = koinViewModel()

    val loginMessageState = remember { mutableStateOf("") }
    val loginState by loginViewModel.loginState.observeAsState(LoginViewModel.LoginUiState.Idle)

    // Coroutine
    val coroutineScope = rememberCoroutineScope()

    // States
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val emailState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()
    var passwordVisible by remember { mutableStateOf(false) }
    val isButtonEnabled by remember {
        derivedStateOf {
            emailState.text.isNotEmpty() &&
                    passwordState.text.isNotEmpty() &&
                    buttonState == ButtonState.Idle
        }
    }

    LaunchedEffect(Unit) {
        loginViewModel.loginEvents.collect { event ->
            when (event) {
                is LoginViewModel.LoginUiEvent.Navigate -> {
                    navController.navigate("${AppNavHost.LoginOtp.name}/${emailState.text}")
                }
            }
        }
    }


    when (loginState) {
        is LoginViewModel.LoginUiState.Loading -> {
            buttonState = ButtonState.Loading
        }

        is LoginViewModel.LoginUiState.Success -> {
            buttonState = ButtonState.Success
            loginMessageState.value = ""
        }

        is LoginViewModel.LoginUiState.Error -> {
            val error = (loginState as LoginViewModel.LoginUiState.Error).error
            loginMessageState.value = error.error
            buttonState = ButtonState.Error
            // Reset button state back to Idle so the user can try again
            buttonState = ButtonState.Idle

        }

        LoginViewModel.LoginUiState.Idle -> {
            buttonState = ButtonState.Idle
            loginMessageState.value = ""
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                buttonState = ButtonState.Idle
                loginMessageState.value = ""
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
Scaffold { innerPadding->
    Column(
        modifier = modifier.fillMaxSize().padding(innerPadding),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(R.drawable.grumble_hub),
                contentDescription = "Grumble Hub Logo"
            )

            Spacer(modifier = Modifier.height(50.dp))
            OutlinedTextField(
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = Modifier.semantics { contentType = ContentType.Username },
                state = emailState,
                shape = RoundedCornerShape(100.dp),
                label = { Text("Enter your email") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_alternate_email_24),
                        contentDescription = "Email Icon",
                        modifier = Modifier.width(24.dp)
                    )
                },
                trailingIcon = {
                    if (emailState.text.isNotEmpty()) {
                        IconButton(onClick = { emailState.clearText() }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_clear_24),
                                contentDescription = "Clear Text",
                                modifier = Modifier.width(24.dp)
                            )
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedSecureTextField(
                modifier = Modifier.semantics { contentType = ContentType.Password },
                state = passwordState,
                shape = RoundedCornerShape(100.dp),
                placeholder = { Text("Password") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_password_24),
                        contentDescription = "Lock Icon",
                        modifier = Modifier.width(24.dp)
                    )
                },
                trailingIcon = {
                    Icon(
                        if (passwordVisible) painterResource(R.drawable.baseline_visibility_24) else painterResource(
                            R.drawable.baseline_visibility_off_24
                        ),
                        contentDescription = "Toggle Password Visibility",
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible },
                    )
                },
                textObfuscationMode = if (passwordVisible) Visible else RevealLastTyped
            )
            Spacer(modifier = Modifier.height(50.dp))

        }
        Box(contentAlignment = Alignment.Center) {
            Button(
                enabled = isButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,

                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                onClick = {
                    coroutineScope.launch {
                        buttonState = ButtonState.Loading
                        loginViewModel.login(
                            LoginRequest(
                                email = emailState.text.toString(),
                                password = passwordState.text.toString()
                            )
                        )
                    }

                }) {
                Text(text = "Log in", fontSize = MaterialTheme.typography.titleLarge.fontSize)

            }
            when (buttonState) {
                ButtonState.Loading -> LoadingIndicator()

                else -> {} // Idle, Success, Error
            }
        }
    }
}

}