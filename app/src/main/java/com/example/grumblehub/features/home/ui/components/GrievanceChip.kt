package com.example.grumblehub.features.home.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GrievanceChip(modifier: Modifier, text: String) {
    SuggestionChip(
        colors = SuggestionChipDefaults.suggestionChipColors().copy(
            containerColor = MaterialTheme.colorScheme.primary,
            labelColor = MaterialTheme.colorScheme.onPrimary,
        ),
        onClick = {  },
        label = { Text(text) },
        border = null
    )
}