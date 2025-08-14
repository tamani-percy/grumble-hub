package com.example.grumblehub.features.home.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grumblehub.core.room.entities.GrievanceEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GrievanceViewModel(private val grievanceRepository: GrievanceRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(GrievanceUiState())
    val uiState: StateFlow<GrievanceUiState> = _uiState.asStateFlow()

    fun createGrievance(grievance: GrievanceDto) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val response = grievanceRepository.createGrievance(grievance)

            response.fold(
                onSuccess = { createdGrievance ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            tag = createdGrievance,
                            error = null,
                            success = "Successfully created the grievance"
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

    fun clearError() = _uiState.update { it.copy(error = null) }
    fun clearGrievance() = _uiState.update { it.copy(tag = null) }
    fun clearSuccess() = _uiState.update { it.copy(success = null) }
}

data class GrievanceUiState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val tag: GrievanceEntity? = null,
    val error: String? = null
)