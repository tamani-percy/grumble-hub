package com.example.grumblehub.features.home.ui.components.tabs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grumblehub.features.grievance.data.Grievance
import com.example.grumblehub.features.home.ui.components.FiltersComponent
import com.example.grumblehub.features.home.ui.components.GrievanceItem
import com.example.grumblehub.base.AppNavHost
import com.example.grumblehub.sharedviewmodels.GrievanceSharedViewModel
import com.example.grumblehub.utils.grievanceImage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonalTab(isSelected: Boolean, data: List<Grievance>, isScrollingState:LazyListState, grievanceSharedViewModel: GrievanceSharedViewModel, navController: NavController) {
    val listState = rememberLazyListState()
    var isScrollingDown by remember { mutableStateOf(false) }

    LaunchedEffect(listState) {
        var previousIndex = listState.firstVisibleItemIndex
        var previousScrollOffset = listState.firstVisibleItemScrollOffset

        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->
            isScrollingDown = if (index != previousIndex) {
                index > previousIndex
            } else {
                offset > previousScrollOffset
            }

            previousIndex = index
            previousScrollOffset = offset
        }
    }
    if (isSelected && data != null) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.onPrimaryContainer)) {
            Spacer(modifier = Modifier.height(15.dp))
            FiltersComponent()
            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn(
                state = isScrollingState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(
                    items = data,
                    key = { grievance -> grievance.id }
                ) { grievance ->
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