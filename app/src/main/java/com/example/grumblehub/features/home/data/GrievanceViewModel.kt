package com.example.grumblehub.features.home.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grumblehub.core.room.entities.GrievanceEntity
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

class GrievanceViewModel(private val grievanceRepository: GrievanceRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(GrievanceUiState())
    val uiState: StateFlow<GrievanceUiState> = _uiState.asStateFlow()

    private val _deleteUiState = MutableStateFlow<DeleteGrievanceUiState>(DeleteGrievanceUiState.Idle)
    val deleteUiState: StateFlow<DeleteGrievanceUiState> = _deleteUiState.asStateFlow()

    val allGrievanceUiState: StateFlow<GrievanceUiState> =
        grievanceRepository.observeGrievances()
            .map { list -> GrievanceUiState(isLoading = false, grievances = list) }
            .onStart { emit(GrievanceUiState(isLoading = true)) }
            .catch { e -> emit(GrievanceUiState(error = e.message)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = GrievanceUiState()
            )

    fun createGrievance(grievance: GrievanceDto) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val response = grievanceRepository.createGrievance(grievance)

            response.fold(
                onSuccess = { createdGrievance ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            grievance = createdGrievance,
                            error = null,
                            success = "Successfully created the grievance"
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            grievance = null,
                            error = throwable.message ?: "Unknown error"
                        )
                    }
                }
            )
        }
    }

    fun getGrievanceById(grievanceId: Long) {
        viewModelScope.launch {
            val res = grievanceRepository.getGrievanceById(grievanceId)
            res.fold(
                onSuccess = { grievance ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            grievance = null,
                            grievanceJoined = grievance,
                            error = null,
                            success = "Successfully created the grievance"
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            grievance = null,
                            error = throwable.message ?: "Unknown error"
                        )
                    }
                }
            )
        }
    }

    fun deleteGrievanceById(grievanceId: Long) {
        viewModelScope.launch {
            _deleteUiState.value = DeleteGrievanceUiState.Loading

            val res = grievanceRepository.deleteGrievanceById(grievanceId)
            res.fold(
                onSuccess = { boolean ->
                    if (boolean) {
                        _deleteUiState.value = DeleteGrievanceUiState.Success
                    } else {
                        _deleteUiState.value = DeleteGrievanceUiState.Error("Failed to delete grievance")
                    }
                },
                onFailure = { exception ->
                    _deleteUiState.value = DeleteGrievanceUiState.Error(
                        exception.message ?: "An unexpected error occurred"
                    )
                }
            )
        }
    }

    fun resetDeleteState() {
        _deleteUiState.value = DeleteGrievanceUiState.Idle
    }
    fun clearError() = _uiState.update { it.copy(error = null) }
    fun clearGrievance() = _uiState.update { it.copy(grievance = null) }
    fun clearSuccess() = _uiState.update { it.copy(success = null) }
}

data class GrievanceUiState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val grievance: GrievanceEntity? = null,
    val grievanceJoined: Grievance? = null,
    val grievances: List<Grievance> = emptyList(),
    val error: String? = null
)

sealed class DeleteGrievanceUiState {
    data object Idle : DeleteGrievanceUiState()
    data object Loading : DeleteGrievanceUiState()
    data object Success : DeleteGrievanceUiState()
    data class Error(val message: String) : DeleteGrievanceUiState()
}
