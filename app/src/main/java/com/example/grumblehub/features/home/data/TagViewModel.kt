package com.example.grumblehub.features.home.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grumblehub.core.room.entities.TagEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TagViewModel(private val tagRepository: TagRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TagUiState())
    val uiState: StateFlow<TagUiState> = _uiState.asStateFlow()

    fun createTag(tag: TagDto) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val response = tagRepository.createTag(tag)

            response.fold(
                onSuccess = { createdTag ->
                    _uiState.update {
                        it.copy(isLoading = false, tag = createdTag, error = null)
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(isLoading = false, tag = null, error = throwable.message ?: "Unknown error")
                    }
                }
            )
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }
    fun clearTag() = _uiState.update { it.copy(tag = null) }
}

data class TagUiState(
    val isLoading: Boolean = false,
    val tag: TagEntity? = null,
    val error: String? = null
)
