package com.example.grumblehub.features.grievance.ui

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.FloatingToolbarDefaults.floatingToolbarVerticalNestedScroll
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.grumblehub.R
import com.example.grumblehub.core.utils.formatEpochMillis
import com.example.grumblehub.core.utils.grievanceMoodGifs
import com.example.grumblehub.features.grievance.ui.components.TopAppBarComponent
import com.example.grumblehub.features.home.data.DeleteGrievanceUiState
import com.example.grumblehub.features.home.data.GrievanceViewModel
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GrievanceScreen(
    context: Context,
    modifier: Modifier,
    navController: NavController,
    grievanceId: Long,
    onNavigateToHome: () -> Unit
) {
    val grievanceViewModel: GrievanceViewModel = koinViewModel()
    val grievanceUiState by grievanceViewModel.uiState.collectAsStateWithLifecycle()
    val deleteUiState by grievanceViewModel.deleteUiState.collectAsState()

    val backgroundRes by rememberSaveable(grievanceId) {
        mutableIntStateOf(GRIEVANCE_BACKGROUNDS.random())
    }

    LaunchedEffect(deleteUiState) {
        when (deleteUiState) {
            is DeleteGrievanceUiState.Success -> {
                onNavigateToHome()
                Toast.makeText(context, "Successfully deleted grievance", Toast.LENGTH_SHORT).show()
                grievanceViewModel.resetDeleteState()
            }

            is DeleteGrievanceUiState.Error -> {
                Toast.makeText(context, "Error deleting grievance", Toast.LENGTH_SHORT).show()
                grievanceViewModel.resetDeleteState()
            }

            else -> {

            }
        }
    }

    LaunchedEffect(grievanceId) {
        grievanceViewModel.getGrievanceById(grievanceId)
    }

    val grievance = grievanceUiState.grievanceJoined

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }
    var expanded by rememberSaveable { mutableStateOf(true) }
    val vibrantColors = FloatingToolbarDefaults.vibrantFloatingToolbarColors()
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(800)) + slideInHorizontally(initialOffsetX = { 800 }),
        exit = fadeOut(animationSpec = tween(800)) + slideOutHorizontally(targetOffsetX = { -800 })
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets(0),
            bottomBar = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (grievance != null) {
                        Text(
                            text = "Date Created: ${formatEpochMillis(grievance.grievance.createdAt)}"
                        )
                    }
                }
            }, topBar = {
                TopAppBarComponent(
                    navController = navController,
                    title = "Grievance Details"
                )
            }) { innerPadding ->
            Box(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(backgroundRes),
                    contentDescription = null,
                    modifier = Modifier
                        .matchParentSize()
                        .alpha(0.5f),
                    contentScale = ContentScale.Fit
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        if (grievance != null) {
                            AsyncImage(
                                filterQuality = FilterQuality.None,
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(grievanceMoodGifs(grievance.mood)).crossfade(true)
                                    .build(),
                                placeholder = painterResource(R.drawable.ic_launcher_background),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Mood Image",
                                modifier = Modifier.size(100.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        FilterChip(
                            selected = true,
                            onClick = { },
                            label = {
                                if (grievance != null) {
                                    Text(
                                        text = "Mood: ${grievance.mood.name}",
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            })
                    }

                }

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .floatingToolbarVerticalNestedScroll(expanded = expanded,
                            onExpand = { expanded = true },
                            onCollapse = { expanded = false })
                        .verticalScroll(rememberScrollState()),
                ) {

                    Spacer(modifier = Modifier.height(100.dp))

                    Spacer(modifier = Modifier.height(50.dp))
                    if (grievance != null) {
                        Text(
                            text = grievance.grievance.grievance,
                        )
                    }
                }
                HorizontalFloatingToolbar(
                    expanded = expanded,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = -ScreenOffset),
                    colors = vibrantColors,
                    content = {
                        IconButton(onClick = { }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_edit_24),
                                contentDescription = "Edit grievance"
                            )
                        }
                        IconButton(onClick = {
                            if (grievance != null) {
                                grievanceViewModel.deleteGrievanceById(grievance.grievance.grievanceId)
                            }
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_delete_24),
                                contentDescription = "Delete grievance"
                            )
                        }
                    },
                )
            }
        }
    }
}

private val GRIEVANCE_BACKGROUNDS = listOf(
    R.drawable.grievance_two,
    R.drawable.grievance_three,
    R.drawable.grievance_four,
    R.drawable.grievance_five
)