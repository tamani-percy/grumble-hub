package com.example.grumblehub.features.home.ui.components

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewTagDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
    isLoading: Boolean = false
) {
    var tagText by remember { mutableStateOf("") }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "New Tag",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "A tag is a keyword or label that you can use to categorize your grievances. You can create a new tag here.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = tagText,
                    onValueChange = { tagText = it },
                    label = {
                        Text("Enter your tag")
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Start),
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = {
                        SparklesText(
                            words = listOf("Work", "Family", "Relationships", "Spiritual"),
                            modifier = Modifier,
                            intervalMillis = 5_000L
                        )
                    }
                )

                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirm(tagText) },
                        enabled = tagText.isNotBlank() || !isLoading,
                    ) {
                        if (isLoading) {
                            LoadingIndicator()
                        } else {
                            Text("Submit")

                        }
                    }
                }
            }
        }
    }
}

//@Composable
//fun SparkleText(text: String) {
//    val infiniteTransition = rememberInfiniteTransition(label = "shimmerTransition")
//    val animatedProgress = infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = InfiniteRepeatableSpec(
//            animation = tween(durationMillis = 1500, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        ), label = "shimmerProgress"
//    )
//
//    val shimmerColors = listOf(
//        Color.LightGray.copy(alpha = 0.6f),
//        Color.White.copy(alpha = 0.9f),
//        Color.LightGray.copy(alpha = 0.6f)
//    )
//
//    val brush = Brush.linearGradient(
//        colors = shimmerColors,
//        start = animatedProgress.value * 500f, // Adjust multiplier for effect speed/range
//        end = (animatedProgress.value + 0.5f) * 500f // Adjust multiplier
//    )
//
//    Text(
//        text = text,
//        style = TextStyle(brush = brush)
//    )
//}