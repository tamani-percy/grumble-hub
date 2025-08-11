package com.example.grumblehub.features.home.ui.components.tabs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grumblehub.base.AppNavHost
import com.example.grumblehub.features.grievance.data.Grievance
import com.example.grumblehub.features.home.ui.components.FiltersComponent
import com.example.grumblehub.features.home.ui.components.PersonalGrievanceItem
import com.example.grumblehub.sharedviewmodels.GrievanceSharedViewModel
import com.example.grumblehub.utils.grievanceImage

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonalTab(
    isSelected: Boolean,
    data: List<Grievance>,
    isLoading: Boolean,
    isScrollingState: LazyListState,
    grievanceSharedViewModel: GrievanceSharedViewModel,
    navController: NavController
) {
    var isScrollingDown by remember { mutableStateOf(false) }

    LaunchedEffect(isScrollingState) {
        var previousIndex = isScrollingState.firstVisibleItemIndex
        var previousScrollOffset = isScrollingState.firstVisibleItemScrollOffset

        snapshotFlow {
            isScrollingState.firstVisibleItemIndex to isScrollingState.firstVisibleItemScrollOffset
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

    if (isSelected) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
        ) {

            Spacer(modifier = Modifier.height(15.dp))
            FiltersComponent()
            Spacer(modifier = Modifier.height(15.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            } else if (data.isNotEmpty()) {
                LazyColumn(
                    state = isScrollingState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    items(
                        items = data.reversed(),
                        key = { grievance -> grievance.id }
                    ) { grievance ->
                        PersonalGrievanceItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5F))
                                .clickable {
                                    grievanceSharedViewModel.selectedGrievance = grievance
                                    navController.navigate(AppNavHost.Grievance.name)
                                },
                            grievance = grievance,
                            image = grievanceImage(grievance.tag)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No grievances have been created.",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}
