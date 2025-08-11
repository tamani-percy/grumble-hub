package com.example.grumblehub.features.grievance.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.grumblehub.R
import com.example.grumblehub.features.grievance.ui.components.TopAppBarComponent

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GrievanceScreen(
    modifier: Modifier,
    navController: NavController,
) {
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
                    horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Date Created: 20/12/2025 08:25"
                    )
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
                        AsyncImage(
                            filterQuality = FilterQuality.None,
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(R.drawable.sad).crossfade(true).build(),
                            placeholder = painterResource(R.drawable.ic_launcher_background),
                            contentScale = ContentScale.Crop,
                            contentDescription = "Mood Image",
                            modifier = Modifier.size(100.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        FilterChip(
                            selected = true,
                            onClick = { },
                            label = {
                                Text(
                                    text = "Mood: Sad",
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
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
                    Text(
                        text = "I am just so tired of everything. Work has been overwhelming, and I feel like I'm drowning in responsibilities. It's like no matter how hard I try, I can't catch a break. I just want to scream and let it all out.",
                    )
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
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_heart),
                                contentDescription = "Heart grievance"
                            )
                        }
                        IconButton(onClick = { /* doSomething() */ }) {
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