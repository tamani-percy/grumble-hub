package com.example.grumblehub.features.home.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.grumblehub.R
import com.example.grumblehub.features.grievance.data.Grievance
import com.example.grumblehub.features.home.ui.components.NewGrievanceDialog
import com.example.grumblehub.features.home.ui.components.NewGrievanceFAB
import com.example.grumblehub.features.home.ui.components.TopBarComponent
import com.example.grumblehub.features.home.ui.components.tabs.GrumblersTab
import com.example.grumblehub.features.home.ui.components.tabs.PersonalTab
import com.example.grumblehub.mock.grumblersGrievances
import com.example.grumblehub.mock.personalGrievances
import com.example.grumblehub.sharedviewmodels.GrievanceSharedViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

data class TabItem(
    val title: String,
    val icon: Int,
    val content: @Composable (isSelected: Boolean, data: Any?) -> Unit
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Suppress("UNCHECKED_CAST")
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    grievanceSharedViewModel: GrievanceSharedViewModel
) {
    val listState = rememberLazyListState()
    var isScrollingDown by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val personalGrievancesMock = personalGrievances
    val grumblersGrievancesMock = grumblersGrievances

    val tabs = listOf(
        TabItem(
            title = "Personal",
            icon = R.drawable.baseline_password_24,
            content = { isSelected, data ->
                PersonalTab(
                    isSelected = isSelected,
                    data = data as List<Grievance>,
                    isScrollingState = listState,
                    grievanceSharedViewModel = grievanceSharedViewModel,
                    navController = navController
                )
            }
        ),
        TabItem(
            title = "Grumblers",
            icon = R.drawable.baseline_person_24,
            content = { isSelected, data ->
                GrumblersTab(
                    isSelected = isSelected,
                    data = data as List<Grievance>,
                    isScrollingState = listState,
                    grievanceSharedViewModel = grievanceSharedViewModel,
                    navController = navController
                )
            }
        )
    )


    LaunchedEffect(Unit) {
        var previousIndex = listState.firstVisibleItemIndex
        var previousScrollOffset = listState.firstVisibleItemScrollOffset

        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }
            .distinctUntilChanged()
            .collect { (index, offset) ->
                isScrollingDown = if (index != previousIndex) {
                    index > previousIndex
                } else {
                    offset > previousScrollOffset
                }

                previousIndex = index
                previousScrollOffset = offset
            }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopBarComponent(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { index -> selectedTabIndex = index },
                tabs = tabs
            )
        },
        floatingActionButton = {
            Row(horizontalArrangement = Arrangement.End) {
                NewGrievanceFAB(
                    scrollingDown = isScrollingDown,
                    modifier = Modifier.fillMaxWidth(),
                    onShowDialog = { showDialog = true }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            val tabData = when (selectedTabIndex) {
                0 -> personalGrievancesMock
                1 -> grumblersGrievancesMock
                else -> emptyList()
            }

            tabs[selectedTabIndex].content(
                true,
                tabData
            )
        }
    }

    if (showDialog) {
        NewGrievanceDialog(
            onDismissRequest = { showDialog = false },
            onConfirm = { showDialog = false }
        )
    }
}