package com.example.grumblehub.features.home.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.grumblehub.R
import com.example.grumblehub.core.datastore.DataStoreManager
import com.example.grumblehub.features.core.AppNavHost
import com.example.grumblehub.features.home.data.GrievanceDto
import com.example.grumblehub.features.home.data.GrievanceViewModel
import com.example.grumblehub.features.home.data.MoodDto
import com.example.grumblehub.features.home.data.MoodViewModel
import com.example.grumblehub.features.home.data.TagDto
import com.example.grumblehub.features.home.data.TagViewModel
import com.example.grumblehub.features.home.ui.components.GrievanceItem
import com.example.grumblehub.features.home.ui.components.NewGrievanceDialog
import com.example.grumblehub.features.home.ui.components.NewMoodDialog
import com.example.grumblehub.features.home.ui.components.NewTagDialog
import com.example.grumblehub.features.home.ui.components.StepData
import com.example.grumblehub.features.home.ui.components.StepperDialog
import com.example.grumblehub.features.home.ui.components.TopBarComponent
import com.example.grumblehub.features.konfetti.KonfettiViewModel
import kotlinx.coroutines.flow.first
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.PartySystem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    dataStoreManager: DataStoreManager,
    konfettiState: KonfettiViewModel.State,
    context: Context,
    heartDrawable: Drawable?,
    konfettiViewModel: KonfettiViewModel
) {
    val tagViewModel: TagViewModel = koinViewModel()
    val moodViewModel: MoodViewModel = koinViewModel()
    val grievanceViewModel: GrievanceViewModel = koinViewModel()

    val tagUiState = tagViewModel.uiState.collectAsStateWithLifecycle()
    val moodUiState = moodViewModel.uiState.collectAsStateWithLifecycle()
    val grievanceUiState = grievanceViewModel.uiState.collectAsStateWithLifecycle()

    val allMoodsUiState by moodViewModel.allMoodsUiState.collectAsStateWithLifecycle()
    val allTagsUiState by tagViewModel.allTagsUiState.collectAsStateWithLifecycle()
    val allGrievanceUiState by grievanceViewModel.allGrievanceUiState.collectAsStateWithLifecycle()

    val hasShownKonfetti = rememberSaveable { mutableStateOf(false) }

    var isStepperCheckDone by remember { mutableStateOf(false) }
    var showStepperDialog by remember { mutableStateOf(true) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    var showGrievanceDialog by remember { mutableStateOf(false) }
    var showTagDialog by remember { mutableStateOf(false) }
    var showMoodDialog by remember { mutableStateOf(false) }

    val images = remember {
        listOf(R.drawable.work, R.drawable.relationship, R.drawable.social)
    }

    val listState = rememberLazyListState()
    val grievances = allGrievanceUiState.grievances

    LaunchedEffect(grievanceUiState.value.success) {
        grievanceUiState.value.success?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            grievanceViewModel.clearSuccess()
            grievanceViewModel.clearGrievance()
            grievanceViewModel.clearError()
            if (heartDrawable != null) {
                konfettiViewModel.explode()
                hasShownKonfetti.value = true
            }
        }
    }

    LaunchedEffect(moodUiState.value.success) {
        moodUiState.value.success?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            moodViewModel.clearSuccess()
            moodViewModel.clearMood()
            moodViewModel.clearError()
            if (heartDrawable != null) {
                konfettiViewModel.explode()
                hasShownKonfetti.value = true
            }
        }
    }

    LaunchedEffect(tagUiState.value.success) {
        tagUiState.value.success?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            tagViewModel.clearSuccess()
            tagViewModel.clearTag()
            tagViewModel.clearError()
            if (heartDrawable != null) {
                konfettiViewModel.explode()
                hasShownKonfetti.value = true
            }
        }
    }

    LaunchedEffect(Unit) {
        val showAgain = dataStoreManager.getStepper().first()
        showStepperDialog = !showAgain
        isStepperCheckDone = true
    }

    if (isStepperCheckDone && showStepperDialog) {
        StepperDialog(
            steps = listOf(
                StepData("Welcome", "This is the first step of your onboarding.", R.drawable.sad),
                StepData("Second Step", "Here's some more information.", R.drawable.happy),
                StepData("All Done!", "You're ready to start using the app.", R.drawable.shush)
            ),
            dataStoreManager = dataStoreManager,
            onDismiss = { showStepperDialog = false }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopBarComponent(
                onClick = {
                    navController.navigate(AppNavHost.Settings.name)
                }
            )
        },
        floatingActionButton = {
            FabMenu(
                listState = listState,
                showTagDialog = { showTagDialog = it },
                showMoodDialog = { showMoodDialog = it },
                showGrievanceDialog = { showGrievanceDialog = it },
            )
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    if (grievances.isEmpty()) {
                        item {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(Modifier.height(200.dp))
                                Image(
                                    painter = painterResource(R.drawable.nothing),
                                    contentDescription = "No grievances",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 60.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                )
                                Spacer(Modifier.height(50.dp))
                                Text(
                                    text = "No grievances yet. Tap + to add one.",
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        items(
                            items = grievances,
                            key = { it.grievance.grievanceId } // must be unique & stable
                        ) { grievance ->
                            GrievanceItem(
                                grievance = grievance,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { grievanceId ->
                                    navController.navigate("${AppNavHost.Grievance.name}/$grievanceId")
                                }
                            )
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                            )
                        }
                    }
                }
            }
            if (showGrievanceDialog) {
                NewGrievanceDialog(
                    moods = allMoodsUiState.moods,
                    tags = allTagsUiState.tags,
                    onDismissRequest = { showGrievanceDialog = false },
                    onConfirm = { grievanceTitle, grievanceText, moodId, tagId ->
                        grievanceViewModel.createGrievance(
                            GrievanceDto(
                                title = grievanceTitle,
                                grievance = grievanceText,
                                moodId = moodId,
                                tagId = tagId,
                            )
                        )
                        showGrievanceDialog = false
                    }
                )
            }

            if (showTagDialog) {
                NewTagDialog(
                    onDismissRequest = { showTagDialog = false },
                    onConfirm = { tag ->
                        tagViewModel.createTag(tag = TagDto(name = tag))
                        showTagDialog = false
                    },
                    isLoading = tagUiState.value.isLoading,
                )
            }

            if (showMoodDialog) {
                NewMoodDialog(
                    onDismissRequest = { showTagDialog = false },
                    onConfirm = { tag ->
                        moodViewModel.createMood(mood = MoodDto(name = tag))
                        showMoodDialog = false
                    },
                    isLoading = moodUiState.value.isLoading,
                )
            }
            if (konfettiState is KonfettiViewModel.State.Started) {
                KonfettiView(
                    modifier = Modifier.fillMaxSize(),
                    parties = konfettiState.party,
                    updateListener = object : OnParticleSystemUpdateListener {
                        override fun onParticleSystemEnded(
                            system: PartySystem,
                            activeSystems: Int,
                        ) {
                            if (activeSystems == 0) konfettiViewModel.ended()
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FabMenu(
    listState: LazyListState,
    showTagDialog: (Boolean) -> Unit,
    showGrievanceDialog: (Boolean) -> Unit,
    showMoodDialog: (Boolean) -> Unit
) {
    val fabVisible by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
    Box {
        val items =
            listOf(
                R.drawable.tag to "Create Tag",
                R.drawable.mood to "Create Mood",
                R.drawable.notes to "Create Grievance",
            )
        var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
        BackHandler(fabMenuExpanded) { fabMenuExpanded = false }
        FloatingActionButtonMenu(
            modifier = Modifier.align(Alignment.BottomEnd),
            expanded = fabMenuExpanded,
            button = {
                ToggleFloatingActionButton(
                    modifier =
                    Modifier
                        .semantics {
                            traversalIndex = -1f
                            stateDescription = if (fabMenuExpanded) "Expanded" else "Collapsed"
                            contentDescription = "Toggle menu"
                        }
                        .animateFloatingActionButton(
                            visible = fabVisible || fabMenuExpanded,
                            alignment = Alignment.BottomEnd,
                        ),
                    checked = fabMenuExpanded,
                    onCheckedChange = { fabMenuExpanded = !fabMenuExpanded },
                ) {
                    val resourceId by remember {
                        derivedStateOf {
                            if (checkedProgress > 0.5f) R.drawable.baseline_close_24 else R.drawable.baseline_add_24
                        }
                    }
                    Icon(
                        painter = painterResource(resourceId),
                        contentDescription = null,
                        modifier = Modifier.animateIcon({ checkedProgress }),
                    )
                }
            },
        ) {
            items.forEachIndexed { i, item ->
                FloatingActionButtonMenuItem(
                    modifier =
                    Modifier.semantics {
                        isTraversalGroup = true
                        if (i == items.size - 1) {
                            customActions =
                                listOf(
                                    CustomAccessibilityAction(
                                        label = "Close menu",
                                        action = {
                                            fabMenuExpanded = false
                                            true
                                        },
                                    )
                                )
                        }
                    },
                    onClick = {
                        fabMenuExpanded = false
                        when (item.second) {
                            "Create Tag" -> {
                                showTagDialog(true)
                            }

                            "Create Mood" -> {
                                showMoodDialog(true)
                            }

                            "Create Grievance" -> {
                                showGrievanceDialog(true)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(item.first),
                            contentDescription = null
                        )
                    },
                    text = { Text(text = item.second) },
                )
            }
        }
    }
}