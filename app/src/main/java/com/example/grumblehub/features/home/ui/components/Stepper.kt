package com.example.grumblehub.features.home.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.grumblehub.core.datastore.DataStoreManager
import kotlinx.coroutines.launch

@Composable
fun StepperDialog(
    steps: List<StepData>,
    onDismiss: () -> Unit,
    dataStoreManager: DataStoreManager,
    startAt: Int = 0,
    dismissOnOutside: Boolean = false,
    showSkip: Boolean = false,
) {
    var currentStep by remember { mutableIntStateOf(startAt.coerceIn(0, steps.lastIndex)) }
    var dontShowAgain by remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = dismissOnOutside)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Step counter + skip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Step ${currentStep + 1} of ${steps.size}")
                    if (showSkip && currentStep < steps.lastIndex) {
                        TextButton(onClick = onDismiss) { Text("Skip") }
                    }
                }

                // Progress bar
                LinearProgressIndicator(
                    progress = { (currentStep + 1f) / steps.size.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(Modifier.height(16.dp))

                // Title
                Text(
                    text = steps[currentStep].title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(10.dp))

                // Image / GIF
                AsyncImage(
                    filterQuality = FilterQuality.None,
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(steps[currentStep].gifUrl)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Fit,
                    contentDescription = "Stepper GIF",
                    modifier = Modifier
                        .size(70.dp)
                        .padding(vertical = 10.dp)
                )

                if (steps[currentStep].description.isNotEmpty()) {
                    Text(
                        text = steps[currentStep].description,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(18.dp))

                StepDotsIndicator(
                    total = steps.size,
                    current = currentStep,
                    onDotClick = { idx -> currentStep = idx }
                )

                Spacer(Modifier.height(18.dp))

                if (currentStep == steps.lastIndex) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Checkbox(
                            checked = dontShowAgain,
                            onCheckedChange = { dontShowAgain = it }
                        )
                        Text("Don't show again")
                    }
                }

                // Navigation buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { if (currentStep > 0) currentStep-- },
                        enabled = currentStep > 0
                    ) { Text("Back") }

                    if (currentStep < steps.lastIndex) {
                        Button(onClick = { currentStep++ }) { Text("Next") }
                    } else {
                        Button(onClick = {
                            if (dontShowAgain) {
                                coroutine.launch {
                                    dataStoreManager.setStepper(true)
                                }
                            }
                            onDismiss()
                        }) { Text("Finish") }
                    }
                }
            }
        }
    }
}


@Composable
private fun StepDotsIndicator(
    total: Int,
    current: Int,
    onDotClick: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(total) { index ->
            val isActive = index == current
            val size by animateDpAsState(
                targetValue = if (isActive) 12.dp else 8.dp,
                animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f),
                label = "dotSize"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(size)
                    .clip(CircleShape)
                    .background(
                        if (isActive)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                    .border(
                        width = if (isActive) 0.dp else 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = CircleShape
                    )
                    .clickable { onDotClick(index) }
            )
        }
    }
}

data class StepData(
    val title: String,
    val description: String,
    val gifUrl: Int? = null
)
