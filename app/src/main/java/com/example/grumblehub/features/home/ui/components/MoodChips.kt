package com.example.grumblehub.features.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.grumblehub.R

data class ChipItem(val id: Int, val label: String)

@Composable
fun SingleSelectionChipGroup(
    chips: List<ChipItem>,
    selectedChipId: Int?, // This is the single source of truth for selection
    onChipSelected: (Int) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between chips
    ) {
        chips.forEach { chip ->
            FilterChip(
                selected = chip.id == selectedChipId, // Check if this chip is currently selected
                onClick = { onChipSelected(chip.id) }, // Trigger the callback with the selected chip's ID
                label = { Text(chip.label) },
                // Modifier.weight(1f) was removed in the previous fix to allow natural sizing
                // No weight modifier is needed here for FlowRow to behave correctly
                leadingIcon = {
                    // Use selectedChipId directly here
                    if (selectedChipId == chip.id) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_done_24),
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                }
            )
        }
    }
}