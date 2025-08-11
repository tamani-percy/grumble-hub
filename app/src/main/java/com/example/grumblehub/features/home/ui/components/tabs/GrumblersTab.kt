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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grumblehub.features.grievance.data.Grievance
import com.example.grumblehub.features.grievance.data.GroupGrievanceResponse
import com.example.grumblehub.features.home.ui.components.FiltersComponent
import com.example.grumblehub.features.home.ui.components.GroupGrievanceItem
import com.example.grumblehub.sharedviewmodels.GrievanceSharedViewModel
import com.example.grumblehub.utils.grievanceImage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GrumblersTab(
    isSelected: Boolean,
    grievances: List<Grievance>,
    listState: LazyListState,
    grievanceSharedViewModel: GrievanceSharedViewModel,
    navController: NavController
) {
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

    if (isSelected) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.onPrimaryContainer)) {
            Spacer(modifier = Modifier.height(15.dp))
            FiltersComponent()
            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(
                    items = grievances,
                    key = { grievance -> grievance}
                ) { grievance ->
                    GroupGrievanceItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                grievanceSharedViewModel.selectedGrievance = grievance
                            },
                        grievance = grievance,
                        image = grievanceImage(grievance.tag)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GrievanceTabs(
    isLoading: Boolean,
    isSelected: Boolean,
    groupGrievances: List<GroupGrievanceResponse>,
    grievanceSharedViewModel: GrievanceSharedViewModel,
    navController: NavController
) {
    val tabs = remember(groupGrievances) {
        groupGrievances
            .groupBy { it.userResponse }
            .entries
            .map { (user, responses) ->
                user to responses.flatMap { it.grievanceResponse }
            }
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabListStates = remember { tabs.map { LazyListState() } }

    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, (user, _) ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(user.email) }
                )
            }
        }

        tabs.forEachIndexed { index, (user, grievances) ->
            GrumblersTab(
                isSelected = isSelected,
                grievances = grievances,
                listState = tabListStates[index],
                grievanceSharedViewModel = grievanceSharedViewModel,
                navController = navController
            )
        }
    }
}
