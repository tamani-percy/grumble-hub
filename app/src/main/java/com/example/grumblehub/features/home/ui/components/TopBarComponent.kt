package com.example.grumblehub.features.home.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.grumblehub.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComponent(
    onClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Grievances",
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            Row {
                IconButton(onClick = {onClick()}) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_settings_24),
                        contentDescription = "Settings icon"
                    )
                }
            }
        },
        windowInsets = WindowInsets(0)
    )
}