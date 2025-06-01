import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextObfuscationMode.Companion.RevealLastTyped
import androidx.compose.foundation.text.input.TextObfuscationMode.Companion.Visible
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalAutofillManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.grumblehub.R
import com.example.grumblehub.features.onboarding.RoundedImageWithColoredShadow
import com.example.grumblehub.fragments.AppNavHost


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val usernameState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()
    val autoFillManager = LocalAutofillManager.current
    var passwordVisible by remember { mutableStateOf(false) }
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

        OutlinedTextField(
            modifier = Modifier.semantics { contentType = ContentType.Username },
            state = usernameState,
            shape = RoundedCornerShape(100.dp),
            label = { Text("Username") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_person_24),
                    contentDescription = "Lock Icon",
                    modifier = Modifier.width(24.dp)
                )
            },
            trailingIcon = {
                if (usernameState.text.isNotEmpty()) {
                    IconButton(onClick = { usernameState.clearText() }) {
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
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,

                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            onClick = {
                navController.navigate(AppNavHost.Otp.name)
            }) {
            Text(text = "Log in", fontSize = MaterialTheme.typography.titleLarge.fontSize)

        }
    }

}