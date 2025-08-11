package com.example.grumblehub.features.home.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.grumblehub.R
import com.example.grumblehub.core.datastore.DataStoreManager
import com.example.grumblehub.features.grievance.data.Grievance
import com.example.grumblehub.features.grievance.data.GrievanceViewModel
import com.example.grumblehub.features.grievance.data.GroupGrievanceResponse
import com.example.grumblehub.features.home.ui.components.NewGrievanceDialog
import com.example.grumblehub.features.home.ui.components.NewGrievanceFAB
import com.example.grumblehub.features.home.ui.components.TopBarComponent
import com.example.grumblehub.features.home.ui.components.tabs.GrievanceTabs
import com.example.grumblehub.features.home.ui.components.tabs.GrumblersTab
import com.example.grumblehub.features.home.ui.components.tabs.PersonalTab
import com.example.grumblehub.sharedviewmodels.GrievanceSharedViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

sealed class TabData {
    data class Personal(val grievances: List<Grievance>) : TabData()
    data class Grumblers(val groupGrievances: List<GroupGrievanceResponse>) : TabData()
}

data class TabItem(
    val title: String,
    val icon: Int,
    val content: @Composable (isSelected: Boolean, data: List<Grievance>?, isLoading: Boolean) -> Unit
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Suppress("UNCHECKED_CAST")
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    grievanceSharedViewModel: GrievanceSharedViewModel,
    dataStoreManager: DataStoreManager
) {

    val snackbarHostState = remember { SnackbarHostState() }


    // View Model Injections
    val grievanceViewModel: GrievanceViewModel = koinViewModel()
    val personalGrievanceState by grievanceViewModel.personalGrievance.observeAsState(initial = GrievanceViewModel.PersonalGrievanceUiState.Idle)
    val nonPersonalGrievanceState by grievanceViewModel.nonPersonalGrievances.observeAsState(initial = GrievanceViewModel.NonPersonalGrievanceUiState.Idle)

    val listState = rememberLazyListState()
    var isScrollingDown by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }


    val scope = rememberCoroutineScope()

    val tabs = listOf(
        TabItem(
            title = "Personal",
            icon = R.drawable.baseline_password_24,
            content = { isSelected, data, _ ->
                PersonalTab(
                    isSelected = isSelected,
                    data = data as List<Grievance>,
                    isLoading = personalGrievanceState is GrievanceViewModel.PersonalGrievanceUiState.Loading,
                    isScrollingState = listState,
                    grievanceSharedViewModel = grievanceSharedViewModel,
                    navController = navController
                )
            }
        ),
        TabItem(
            title = "Grumblers",
            icon = R.drawable.baseline_person_24,
            content = { isSelected, data, _ ->
                GrievanceTabs(
                    isSelected = isSelected,
                    isLoading = nonPersonalGrievanceState is GrievanceViewModel.NonPersonalGrievanceUiState.Loading,
                    groupGrievances = data as List<GroupGrievanceResponse>,
                    grievanceSharedViewModel = grievanceSharedViewModel,
                    navController = navController
                )

            }
        )
    )

    LaunchedEffect(Unit) {
        grievanceViewModel.getAllPersonalGrievances()
    }

    LaunchedEffect(Unit) {
        grievanceViewModel.getAllNonPersonalGrievances()
    }

    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex == 0) {
            grievanceViewModel.getAllPersonalGrievances()
        }
    }

    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex == 1) {
            grievanceViewModel.getAllNonPersonalGrievances()
        }
    }
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
        when (val state = personalGrievanceState) {
            is GrievanceViewModel.PersonalGrievanceUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding()),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }


            is GrievanceViewModel.PersonalGrievanceUiState.Success -> {
                val pullToRefreshState = rememberPullToRefreshState()
                var isRefreshing by remember { mutableStateOf(false) }

                LaunchedEffect(isRefreshing) {
                    if (isRefreshing) {
                        grievanceViewModel.refreshGrievances()
                        isRefreshing = false
                    }
                }



                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = { isRefreshing = true },
                    state = pullToRefreshState,
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding())
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        when (selectedTabIndex) {
                            0 -> {
                                tabs[selectedTabIndex].content(
                                    true,
                                    state.response.grievance,
                                    false
                                )
                            }

                            1 -> {
                                tabs[selectedTabIndex].content(
                                    true,
                                    null,
                                    false
                                )
                            }

                            else -> {
                                tabs[selectedTabIndex].content(
                                    true,
                                    emptyList<Grievance>(),
                                    false
                                )
                            }
                        }

                    }
                }
            }


            is GrievanceViewModel.PersonalGrievanceUiState.Error -> {
                Column(
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding())
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Text("Error: ${state.error}")
                }
            }

            else -> Unit
        }
    }


    if (showDialog) {
        NewGrievanceDialog(
            onDismissRequest = { showDialog = false },
            onConfirm = { showDialog = false },
            grievanceViewModel = grievanceViewModel,
            dataStoreManager = dataStoreManager,
            snackbarHostState = snackbarHostState,
            scope = scope
        )
    }
}