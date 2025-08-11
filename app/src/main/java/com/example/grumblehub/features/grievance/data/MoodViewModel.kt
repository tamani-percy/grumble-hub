package com.example.grumblehub.features.grievance.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grumblehub.sharedviewmodels.ErrorResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MoodViewModel(private val moodRepository: MoodRepository) : ViewModel() {

    private val _moodEvents = MutableSharedFlow<MoodUiEvent>()
    val moodEvents = _moodEvents.asSharedFlow()

    private val _moodState = MutableLiveData<MoodUiState>()
    val moodState: LiveData<MoodUiState> = _moodState

     fun getAllMoods() {
        _moodState.value = MoodUiState.Loading

        viewModelScope.launch {
            val mood = moodRepository.getAllMoods()

            mood.fold(
                onSuccess = { response ->
                    if (response.isNotEmpty()) {
                        _moodState.value = MoodUiState.Success(
                            response = response
                        )
                    } else {
                        _moodState.value = MoodUiState.Error(
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

                    _moodState.value = MoodUiState.Error(error)
                }
            )
        }
    }


    sealed class MoodUiEvent {
        data object Navigate : MoodUiEvent()
        data object RefreshRequested : MoodUiEvent()
    }

    sealed class MoodUiState {
        data object Loading : MoodUiState()
        data class Success(val response: List<MoodResponse>) : MoodUiState()
        data class Error(val error: ErrorResponse) : MoodUiState()
        data object Idle : MoodUiState()
    }
}