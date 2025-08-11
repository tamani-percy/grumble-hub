package com.example.grumblehub.features.home.ui.components

import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GrievanceChip(modifier: Modifier, text: String) {
    SuggestionChip(
        onClick = {  },
        label = { Text(text) }
    )
}