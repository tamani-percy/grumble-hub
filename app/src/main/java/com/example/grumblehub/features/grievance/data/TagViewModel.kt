package com.example.grumblehub.features.grievance.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grumblehub.features.grievance.data.MoodViewModel.MoodUiState
import com.example.grumblehub.sharedviewmodels.ErrorResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class TagViewModel(private val tagRepository: TagRepository) : ViewModel() {


    private val _tagEvents = MutableSharedFlow<TagUiEvent>()
    val tagEvents = _tagEvents.asSharedFlow()

    private val _tagState = MutableLiveData<TagUiState>()
    val tagState: LiveData<TagUiState> = _tagState

    fun getAllTags() {
        _tagState.value = TagUiState.Loading
        viewModelScope.launch {
            val tags = tagRepository.getAllTags()

            tags.fold(
                onSuccess = { response ->
                    if (response.isNotEmpty()) {
                        _tagState.value = TagUiState.Success(
                            response = response
                        )
                    } else {
                        _tagState.value = TagUiState.Error(
                            ErrorResponse(
                                "Signup failed with status ${response}",
                                null,
                                500
                            )
                        )
                    }
                },
                onFailure = { throwable ->
                    val error = if (throwable.message?.contains("Malformed") == true) {
                        ErrorResponse("Something went wrong. Please try again later.", null, 500)
                    } else {
                        ErrorResponse(throwable.message ?: "Unknown error", null, 400)
                    }

                    _tagState.value = TagUiState.Error(error)
                }
            )
        }
    }

    sealed class TagUiEvent {
        data object Navigate : TagUiEvent()
        data object RefreshRequested : TagUiEvent()
    }

    sealed class TagUiState {
        data object Loading : TagUiState()
        data class Success(val response: List<TagResponse>) : TagUiState()
        data class Error(val error: ErrorResponse) : TagUiState()
        data object Idle : TagUiState()
    }
}