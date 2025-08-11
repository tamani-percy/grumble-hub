package com.example.grumblehub.features.home.ui.components

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
    val dateOptions = remember { listOf("All", "12/07/2025", "13/07/2025") }
    val moodOptions = remember { listOf("All", "Happy", "Sad", "Angry") }
    val tagOptions = remember { listOf("All", "Work", "Home", "Family") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Dropdown(label = "Date", options = dateOptions, defaultSelection = "All")
            }
            Column(modifier = Modifier.weight(1f)) {
                Dropdown(label = "Mood", options = moodOptions, defaultSelection = "All")
            }
            Column(modifier = Modifier.weight(1f)) {
                Dropdown(label = "Tags", options = tagOptions, defaultSelection = "All")
            }
        }
    }
}

// Optimized Dropdown with stable keys
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(
    label: String,
    options: List<String>,
    defaultSelection: String? = null
) {
    // Create a stable key for this dropdown instance
    val dropdownKey = remember { "$label-dropdown" }
    var expanded by remember(dropdownKey) { mutableStateOf(false) }
    var selectedOption by remember(dropdownKey, options, defaultSelection) {
        mutableStateOf(
            when {
                defaultSelection != null && options.contains(defaultSelection) -> defaultSelection
                options.isNotEmpty() -> options.first()
                else -> ""
            }
        )
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
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
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEachIndexed { _, option ->
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
