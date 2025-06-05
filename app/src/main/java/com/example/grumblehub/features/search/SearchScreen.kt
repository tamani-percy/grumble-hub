package com.example.grumblehub.features.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grumblehub.features.home.ui.components.GrievanceItem
import com.example.grumblehub.features.search.components.SearchBar
import com.example.grumblehub.base.AppNavHost
import com.example.grumblehub.mock.personalGrievances
import com.example.grumblehub.sharedviewmodels.GrievanceSharedViewModel
import com.example.grumblehub.utils.grievanceImage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchScreen(
    modifier: Modifier,
    navController: NavController,
    grievanceSharedViewModel: GrievanceSharedViewModel
) {
    var query by rememberSaveable { mutableStateOf("") }

    val filteredResults = remember(query, personalGrievances) {
        if (query.isBlank()) emptyList()
        else personalGrievances.filter {
            it.grievance.contains(query, ignoreCase = true) ||
                    it.title.contains(query, ignoreCase = true)
        }
    }
    SearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = { /* optional: perform search logic here */ },
        placeholderText = "Search people..."
    ) {
        if (filteredResults.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredResults) { grievance ->
                    GrievanceItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                grievanceSharedViewModel.selectedGrievance = grievance
                                navController.navigate(AppNavHost.Grievance.name)
                            },
                        grievance = grievance,
                        image = grievanceImage(grievance.tag)
                    )
                }
            }
        }
    }
}
