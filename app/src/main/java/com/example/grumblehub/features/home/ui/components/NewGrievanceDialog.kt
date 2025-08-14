package com.example.grumblehub.features.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.grumblehub.core.room.entities.MoodEntity

@Composable
fun NewGrievanceDialog(
    moods: List<MoodEntity>,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var grievanceText by remember { mutableStateOf("") }
    var selectedMoodId by remember { mutableStateOf<Int?>(null) }
    var selectedTagId by remember { mutableStateOf<Int?>(null) }

    val moodChipItems = remember(moods) {
        moods.map { mood ->
            ChipItem(
                id = mood.moodId.toInt(),
                label = mood.name
            )
        }
    }

    val tagChipItems = remember {
        listOf(
            ChipItem(1, "Work"),
            ChipItem(2, "Home"),
            ChipItem(3, "Social"),
            ChipItem(4, "Relationships"),
        )
    }

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
                    text = "New Grievance",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = grievanceText,
                    onValueChange = { grievanceText = it },
                    label = { Text("Enter your grievance") },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Start),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Mood",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                SingleSelectionChipGroup(
                    chips = moodChipItems,
                    selectedChipId = selectedMoodId,
                    onChipSelected = { newId ->
                        selectedMoodId = newId
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tags",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                SingleSelectionChipGroup(
                    chips = tagChipItems,
                    selectedChipId = selectedTagId,
                    onChipSelected = { newId ->
                        selectedTagId = newId
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirm(grievanceText) },
                        enabled = grievanceText.isNotBlank() && selectedMoodId != null && selectedTagId != null,
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}
