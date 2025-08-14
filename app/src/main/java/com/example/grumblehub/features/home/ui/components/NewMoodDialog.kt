package com.example.grumblehub.features.home.ui.components

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewMoodDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
    isLoading: Boolean = false
) {
    var moodText by remember { mutableStateOf("") }
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
                    text = "New Mood",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "A mood is basically a feeling. Use it to give a grievance a certain emotion.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = moodText,
                    onValueChange = { moodText = it },
                    label = {
                        Text("Enter your mood")
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Start),
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = {
                        SparklesText(
                            words = listOf(
                                "Happy",
                                "Sad",
                                "Disgusted",
                                "Meh",
                                "Upset",
                                "Unamused",
                                "Shock",
                                "Tired"
                            ),
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
                        onClick = { onConfirm(moodText) },
                        enabled = moodText.isNotBlank() || !isLoading,
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