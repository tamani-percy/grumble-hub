package com.example.grumblehub.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grumblehub.R
import com.example.grumblehub.core.AppNavHost
import com.example.grumblehub.features.home.components.FiltersComponent
import com.example.grumblehub.features.home.components.GrievanceItem
import com.example.grumblehub.features.home.components.NewGrievanceDialog
import com.example.grumblehub.features.home.components.NewGrievanceFAB
import com.example.grumblehub.features.home.components.TopBarComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier, navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var showDialog by remember { mutableStateOf(false) } // State to control dialog visibility
    val results = listOf<String>()
    val allItems = remember {
        listOf(
            "Apple", "Banana", "Cherry", "Elderberry",
            "Fig", "Grape", "Honeydew", "Jackfruit", "Kiwi"
        )
    }

    val images = remember {
        listOf(R.drawable.work, R.drawable.relationship, R.drawable.social)
    }


    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = { TopBarComponent() },
        floatingActionButton = {
            // Pass the lambda to update showDialog when FAB is clicked
            Row(horizontalArrangement = Arrangement.End) {
                NewGrievanceFAB(
                    modifier = Modifier.fillMaxWidth(),
                    onShowDialog = { showDialog = true })
            }
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize()
            ) {

                FiltersComponent()
                Spacer(modifier = Modifier.height(15.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    itemsIndexed(
                        items = results.ifEmpty { allItems },
                        key = { _, item -> item }) { index, _ ->
                        GrievanceItem(
                            modifier = Modifier
                                .fillMaxWidth(),
                            image = images.getOrElse(index % images.size) { R.drawable.ic_launcher_foreground },
                            text = "Work",
                            navController = navController,
                            onClick = {
                                navController.navigate(AppNavHost.Grievance.name)
                            }
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                        )
                    }
                }
            }
            if (showDialog) {
                NewGrievanceDialog(
                    onDismissRequest = { showDialog = false }, // Dismiss dialog
                    onConfirm = {
                        showDialog = false // Dismiss dialog after confirmation
                    }
                )
            }
        }
    }
}

