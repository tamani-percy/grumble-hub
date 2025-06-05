package com.example.grumblehub.features.onboarding

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.grumblehub.R
import com.example.grumblehub.features.konfetti.KonfettiViewModel
import com.example.grumblehub.base.AppNavHost
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.PartySystem
import nl.dionsegijn.konfetti.xml.image.ImageUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(modifier: Modifier, navController: NavHostController) {
    // ViewModel for Konfetti
    val viewModel = viewModel<KonfettiViewModel>()
    val state: KonfettiViewModel.State by viewModel.state.observeAsState(
        KonfettiViewModel.State.Idle,
    )
    val context = LocalContext.current
    val drawable = AppCompatResources.getDrawable(context, R.drawable.ic_heart)
    val hasShownKonfetti = rememberSaveable { mutableStateOf(false) }

    // Dropdown Menu
    var expanded by remember { mutableStateOf(false) }

    // Show Konfetti when the screen is launched
    LaunchedEffect(Unit) {
        if (!hasShownKonfetti.value && drawable != null) {
            viewModel.festive(ImageUtil.loadDrawable(drawable))
            hasShownKonfetti.value = true
        }
    }

    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                painter = painterResource(R.drawable.baseline_more_vert_24),
                contentDescription = "More options"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Home Screen") },
                onClick = { navController.navigate(AppNavHost.Home.name) }
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.grumble_hub),
                contentDescription = "Grumble Hub Logo"
            )
            Text(
                text = "Your one-stop destination to relieve that kandolo in your throat.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 15.dp)
            )

            RoundedImageWithColoredShadow(
                imageResourceId = R.drawable.booboo
            )

            Button(
                onClick = {
                    navController.navigate(AppNavHost.Signup.name)
                }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_person_24),
                    contentDescription = "Arrow Forward Icon",
                    modifier = Modifier.width(24.dp)
                )
                Text(text = "Sign up", fontSize = MaterialTheme.typography.titleLarge.fontSize)

            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,

                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                onClick = {
                    navController.navigate(AppNavHost.Login.name)
                }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_right_alt_24),
                    contentDescription = "Arrow Forward Icon",
                    modifier = Modifier.width(24.dp)
                )
                Text(text = "Log in", fontSize = MaterialTheme.typography.titleLarge.fontSize)

            }
        }

        if (state is KonfettiViewModel.State.Started) {
            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = (state as KonfettiViewModel.State.Started).party,
                updateListener = object : OnParticleSystemUpdateListener {
                    override fun onParticleSystemEnded(
                        system: PartySystem,
                        activeSystems: Int,
                    ) {
                        if (activeSystems == 0) viewModel.ended()
                    }
                }
            )
        }
    }
}


@Composable
fun RoundedImageWithColoredShadow(
    modifier: Modifier = Modifier,
    imageResourceId: Int,
) {
    Box(
        modifier = modifier
            .padding(20.dp)
            .clip(RoundedCornerShape(100.dp))
            .size(150.dp)
    ) {
        Image(
            painter = painterResource(id = imageResourceId),
            contentDescription = "Rounded Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
