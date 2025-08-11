import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.grumblehub.R
import com.example.grumblehub.base.AppNavHost
import com.example.grumblehub.core.apis.AuthService
import com.example.grumblehub.features.signup.data.SignupRepository
import com.example.grumblehub.features.signup.data.SignupViewModel
import com.example.grumblehub.features.signup.data.SignupRequest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

enum class ButtonState {
    Idle, Loading, Success, Error
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val signupViewModel:SignupViewModel = koinViewModel()

    var buttonState by remember { mutableStateOf(ButtonState.Idle) }

    val signupMessageState = remember { mutableStateOf("") }

    class SignupViewModelFactory(
        private val signupRepository: SignupRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
                return SignupViewModel(signupRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    val signupState by signupViewModel.signupState.observeAsState(SignupViewModel.SignupUiState.Idle)

    val coroutineScope = rememberCoroutineScope()
    val emailState = rememberTextFieldState()

    // Reset button state and message on first composition (or on navigating back)
    LaunchedEffect(Unit) {
        buttonState = ButtonState.Idle
        signupMessageState.value = ""
    }

    // Collect signup events once at the top-level
    LaunchedEffect(Unit) {
        signupViewModel.signupEvents.collect { event ->
            when (event) {
                is SignupViewModel.SignupEvent.NavigateToDashboard -> {
                    navController.navigate("${AppNavHost.SignupOtp.name}/${emailState.text}")
                }
            }
        }
    }

    LaunchedEffect(emailState.text) {
        if (emailState.text.isNotEmpty()) {
            if (signupMessageState.value.isNotEmpty()) {
                signupMessageState.value = ""
            }
            if (buttonState == ButtonState.Error || buttonState == ButtonState.Success) {
                buttonState = ButtonState.Idle
            }
        }
    }

    // Update button state and error text based on signup state
    LaunchedEffect(signupState) {
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

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                buttonState = ButtonState.Idle
                signupMessageState.value = ""
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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

            Text(text = "Please sign up to continue")
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
                enabled = emailState.text.isNotEmpty() && buttonState == ButtonState.Idle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                onClick = {
                    coroutineScope.launch {
                        buttonState = ButtonState.Loading
                        // No delay needed, just call the signup function
                        signupViewModel.signupAndSendOtp(SignupRequest(email = emailState.text.toString()))
                    }
                }
            ) {
                Text("Sign up", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            }

            when (buttonState) {
                ButtonState.Loading -> LoadingIndicator()

                else -> {} // Idle, Success, Error
            }
        }
    }
}


