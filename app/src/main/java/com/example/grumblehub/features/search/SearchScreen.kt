package com.example.grumblehub.features.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.grumblehub.sharedviewmodels.GrievanceSharedViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchScreen(
    modifier: Modifier,
    navController: NavController,
    grievanceSharedViewModel: GrievanceSharedViewModel
) {
//    var query by rememberSaveable { mutableStateOf("") }
//
//    val filteredResults = remember(query, personalGrievances) {
//        if (query.isBlank()) emptyList()
//        else personalGrievances.filter {
//            it.grievance.contains(query, ignoreCase = true) ||
//                    it.title.contains(query, ignoreCase = true)
//        }
//    }
//    SearchBar(
//        query = query,
//        onQueryChange = { query = it },
//        onSearch = { /* optional: perform search logic here */ },
//        placeholderText = "Search people..."
//    ) {
//        if (filteredResults.isNotEmpty()) {
//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                items(filteredResults) { grievance ->
//                    GrievanceItem(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//                                grievanceSharedViewModel.selectedGrievance = grievance
//                                navController.navigate(AppNavHost.Grievance.name)
//                            },
//                        grievance = grievance,
//                        image = grievanceImage(grievance.tag)
//                    )
//                }
//            }
//        }
//    }
}
