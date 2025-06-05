package com.example.grumblehub.features.grievance.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.grumblehub.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarComponent(navController: NavController, title:String?) {
    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0),
        title = {
            if (title != null) {
                Text(
                    text = title
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {navController.popBackStack() }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = "Go back"
                )
            }
        },
    )
}