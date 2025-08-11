package com.example.grumblehub.features.home.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewModelScope
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.grumblehub.R
import com.example.grumblehub.core.datastore.DataStoreManager
import com.example.grumblehub.features.grievance.data.Grievance
import com.example.grumblehub.features.grievance.data.GrievanceRequest
import com.example.grumblehub.features.grievance.data.GrievanceViewModel
import com.example.grumblehub.features.grievance.data.MoodViewModel
import com.example.grumblehub.features.grievance.data.MoodViewModel.MoodUiState
import com.example.grumblehub.features.grievance.data.TagViewModel
import com.example.grumblehub.features.grievance.data.TagViewModel.TagUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewGrievanceDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (Grievance) -> Unit,
    grievanceViewModel: GrievanceViewModel,
    dataStoreManager: DataStoreManager,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val moodViewModel: MoodViewModel = koinViewModel()
    val moodState by moodViewModel.moodState.observeAsState()

    val tagViewModel: TagViewModel = koinViewModel()
    val tagState by tagViewModel.tagState.observeAsState()

    // Grievance Text State
    val grievanceText = rememberTextFieldState()
    val grievanceTitle = rememberTextFieldState()

    // State for mood chips, initially empty
    var moodChipItems by remember { mutableStateOf(emptyList<ChipItem>()) }
    var tagChipItems by remember { mutableStateOf(emptyList<ChipItem>()) }
    var isPersonal by remember { mutableStateOf(true) }
    var selectedMoodId by remember { mutableStateOf<Long?>(null) }
    var selectedTagId by remember { mutableStateOf<Long?>(null) }

    var showLoading by remember { mutableStateOf(false) }
    val createGrievanceState by grievanceViewModel.createGrievanceState.observeAsState()

    LaunchedEffect(moodState) {
        if (moodState == null || moodState is MoodUiState.Idle) {
            moodViewModel.getAllMoods()
        }

        if (moodState is MoodUiState.Success) {
            val moods = (moodState as MoodUiState.Success).response
            moodChipItems = moods.map { moodResponse ->
                ChipItem(
                    id = moodResponse.id,
                    name = moodResponse.name,
                    createdAt = moodResponse.createdAt
                )
            }
        }
    }

    LaunchedEffect(tagState) {
        if (tagState == null || tagState is TagUiState.Idle) {
            tagViewModel.getAllTags()
        }

        if (tagState is TagUiState.Success) {
            val tags = (tagState as TagUiState.Success).response
            tagChipItems = tags.map { tagResponse ->
                ChipItem(
                    id = tagResponse.id,
                    name = tagResponse.name,
                    createdAt = tagResponse.createdAt
                )
            }
        }
    }

    LaunchedEffect(createGrievanceState) {
        when (val state = createGrievanceState) {
            is GrievanceViewModel.CreateGrievanceUiState.Success -> {
                showLoading = false
                println("DEBUG: Success state reached, showing snackbar")
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Grievance created successfully 😊",
                        actionLabel = "Close",
                        duration = SnackbarDuration.Short
                    )
                }
                grievanceViewModel.resetCreateGrievanceState()
                onConfirm(state.response)
                grievanceViewModel.refreshGrievances()
            }

            is GrievanceViewModel.CreateGrievanceUiState.Error -> {
                showLoading = false
                scope.launch {
                    snackbarHostState.showSnackbar(state.error.error)
                }
                grievanceViewModel.resetCreateGrievanceState()
            }

            is GrievanceViewModel.CreateGrievanceUiState.Loading -> {
                showLoading = true
                println("DEBUG: Loading state")
            }

            else -> {
                println("DEBUG: Other state: $state")
            }
        }
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "New Grievance",
                    style = MaterialTheme.typography.titleLarge,
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                Text(
                    text = "Title",
                    style = MaterialTheme.typography.titleMedium,
                )
                OutlinedTextField(
                    state = grievanceTitle,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    label = { Text("Enter a title") },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Start),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Grievance",
                    style = MaterialTheme.typography.titleMedium,
                )
                OutlinedTextField(
                    state = grievanceText,
                    label = { Text("Enter your grievance") },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Start),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Mood",
                    style = MaterialTheme.typography.titleMedium,
                )
                SingleSelectionChipGroup(
                    chips = moodChipItems,
                    selectedChipId = selectedMoodId,
                    onChipSelected = { newId ->
                        selectedMoodId = newId
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tags",
                    style = MaterialTheme.typography.titleMedium,
                )
                SingleSelectionChipGroup(
                    chips = tagChipItems,
                    selectedChipId = selectedTagId,
                    onChipSelected = { newId ->
                        selectedTagId = newId
                    }
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Is Personal")
                            Checkbox(
                                checked = isPersonal,
                                onCheckedChange = { isPersonal = it }
                            )
                        }

                        if (isPersonal) {
                            AsyncImage(
                                filterQuality = FilterQuality.None,
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(R.drawable.shush).crossfade(true).build(),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Grievance is personal",
                                modifier = Modifier.size(50.dp)
                            )
                        } else {
                            AsyncImage(
                                filterQuality = FilterQuality.None,
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(R.drawable.eyes).crossfade(true).build(),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Grievance is public",
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            grievanceViewModel.viewModelScope.launch {
                                val currentUserId = dataStoreManager.getUserId().first()
                                val grievanceRequest = currentUserId?.let { uid ->
                                    selectedMoodId?.let { moodId ->
                                        selectedTagId?.let { tagId ->
                                            GrievanceRequest(
                                                userId = uid,
                                                isPersonal = isPersonal,
                                                title = grievanceTitle.text.toString(),
                                                grievance = grievanceText.text.toString(),
                                                moodId = moodId,
                                                tagId = tagId,
                                            )
                                        }
                                    }
                                }
                                grievanceRequest?.let {
                                    grievanceViewModel.createGrievance(it)
                                }
                            }
                        },
                        enabled = !showLoading && grievanceText.text.isNotBlank() && grievanceTitle.text.isNotBlank()
                                && selectedMoodId != null && selectedTagId != null
                    ) {
                        if (showLoading) {
                            LoadingIndicator(
                                modifier = Modifier.size(18.dp),
                            )
                        } else {
                            Text("Submit")
                        }
                    }
                }
            }
        }
    }
}