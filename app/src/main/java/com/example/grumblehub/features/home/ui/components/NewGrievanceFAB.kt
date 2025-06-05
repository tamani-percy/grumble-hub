package com.example.grumblehub.features.home.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grumblehub.R

@Composable
fun NewGrievanceFAB(
    modifier: Modifier = Modifier,
    onShowDialog: () -> Unit,
    scrollingDown: Boolean
) {
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primary,
        onClick = onShowDialog,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 10.dp,
            pressedElevation = 15.dp,
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_add_24),
                contentDescription = "Add New Grievance",
                modifier = Modifier.size(30.dp)
            )

            AnimatedVisibility(
                visible = !scrollingDown,
            ) {
                Text(
                    fontSize = 16.sp,
                    text = "New Grievance",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}