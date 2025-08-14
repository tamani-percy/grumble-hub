package com.example.grumblehub.features.home.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grumblehub.core.room.entities.MoodEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoodViewModel(private val moodRepository: MoodRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MoodUiState())
    val uiState: StateFlow<MoodUiState> = _uiState.asStateFlow()
    val allMoodsUiState: StateFlow<MoodUiState> =
        moodRepository.observeMoods()
            .map { list -> MoodUiState(isLoading = false, moods = list) }
            .onStart { emit(MoodUiState(isLoading = true)) } // shows initial loading=true from default state
            .catch { e -> emit(MoodUiState(error = e.message)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MoodUiState()
            )

    init {
        getAllMoods()
    }

    fun createMood(mood: MoodDto) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val response = moodRepository.createMood(mood)

            response.fold(
                onSuccess = { createdMood ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            tag = createdMood,
                            error = null,
                            success = "Successfully created the mood ${createdMood.name}"
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            tag = null,
                            error = throwable.message ?: "Unknown error"
                        )
                    }
                }
            )
        }
    }

    private fun getAllMoods() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val res = moodRepository.getMoods()
            res.fold(
                onSuccess = { moodList ->
                    _uiState.update { it.copy(isLoading = false, moods = moodList) }
                },
                onFailure = { throwable ->
                    _uiState.update { it.copy(isLoading = false, error = throwable.message) }
                }
            )
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }
    fun clearMood() = _uiState.update { it.copy(tag = null) }
    fun clearSuccess() = _uiState.update { it.copy(success = null) }
}

data class MoodUiState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val tag: MoodEntity? = null,
    val moods: List<MoodEntity> = emptyList(),
    val error: String? = null
)

