package com.example.grumblehub.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun FiltersComponent() {
    val dateOptions = listOf("12/07/2025", "13/07/2025")
    val moodOptions = listOf("Happy", "Sad", "Angry")
    val tagOptions = listOf("Work", "Home", "Family")
    Column(
        modifier = Modifier
            .fillMaxWidth().padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(), // Add some padding for better aesthetics
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Each Dropdown is wrapped in a Column with weight(1f)
            Column(modifier = Modifier.weight(1f)) {
                Dropdown(label = "Date", options = dateOptions)
            }
            Column(modifier = Modifier.weight(1f)) {
                Dropdown(label = "Mood", options = moodOptions)
            }
            Column(modifier = Modifier.weight(1f)) {
                Dropdown(label = "Tags", options = tagOptions)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(
    label: String,
    options: List<String>,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            label = { Text(label) },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.labelMedium.fontSize
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .padding(bottom = 10.dp),
            shape = RoundedCornerShape(15.dp),
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            fontSize = MaterialTheme.typography.labelMedium.fontSize
                        )
                    },
                    onClick = {
                        selectedOption = option
                        expanded = false
                    }
                )
            }
        }
    }
}
