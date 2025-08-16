package com.example.grumblehub.features.home.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grumblehub.core.room.entities.TagEntity
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

class TagViewModel(private val tagRepository: TagRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TagUiState())
    val uiState: StateFlow<TagUiState> = _uiState.asStateFlow()
    val allTagsUiState: StateFlow<TagUiState> =
        tagRepository.observeTags()
            .map { list -> TagUiState(isLoading = false, tags = list) }
            .onStart { emit(TagUiState(isLoading = true)) }
            .catch { e -> emit(TagUiState(error = e.message)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = TagUiState()
            )

    fun createTag(tag: TagDto) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val response = tagRepository.createTag(tag)

            response.fold(
                onSuccess = { createdTag ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            tag = createdTag,
                            error = null,
                            success = "Successfully created the tag ${createdTag.name}"
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
    fun clearTag() = _uiState.update { it.copy(tag = null) }
    fun clearSuccess() = _uiState.update { it.copy(success = null) }
}

data class TagUiState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val tags: List<TagEntity> = emptyList(),
    val tag: TagEntity? = null,
    val error: String? = null
)
