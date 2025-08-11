package com.example.grumblehub.features.home.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.grumblehub.R

@Composable
fun NewGrievanceFAB(modifier: Modifier, onShowDialog: () -> Unit) { // Ensure onShowDialog is not optional
    FloatingActionButton(
        onClick = onShowDialog, // This will now trigger the showDialog = true in HomeScreen
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 10.dp,
            pressedElevation = 15.dp,
        )
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Icon(
                painter = painterResource(R.drawable.baseline_add_24),
                contentDescription = "Add New Grievance",
            )
            Text(text = "New Grievance")
        }
    }
}