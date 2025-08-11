package com.example.grumblehub.features.grievance.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grumblehub.core.datastore.DataStoreManager
import com.example.grumblehub.features.grievance.data.MoodViewModel.MoodUiState
import com.example.grumblehub.sharedviewmodels.ErrorResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GrievanceViewModel(
    private val grievanceRepository: GrievanceRepository
) : ViewModel() {

    private val _grievanceEvents = MutableSharedFlow<GrievanceUiEvent>()
    val grievanceEvents = _grievanceEvents.asSharedFlow()

    private val _personalGrievanceState = MutableLiveData<PersonalGrievanceUiState>()
    val personalGrievance: LiveData<PersonalGrievanceUiState> = _personalGrievanceState

    private val _nonPersonalGrievances = MutableLiveData<NonPersonalGrievanceUiState>()
    val nonPersonalGrievances: LiveData<NonPersonalGrievanceUiState> = _nonPersonalGrievances

    private val _createGrievanceState = MutableLiveData<CreateGrievanceUiState>()
    val createGrievanceState: LiveData<CreateGrievanceUiState> = _createGrievanceState

    // Cache variables
    private var cachedPersonalGrievances: UserGrievanceResponse? = null
    private var cachedNonPersonalGrievances: List<GroupGrievanceResponse>? = null
    private var lastFetchTime: Long = 0L
    private val cacheValidityDuration = 5 * 60 * 1000L // 5 minutes in milliseconds

    /**
     * Get all personal grievances with caching
     * @param forceRefresh - if true, bypasses cache and fetches fresh data
     */
    fun getAllPersonalGrievances(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()

            // Check if we can use cached data
            if (!forceRefresh &&
                cachedPersonalGrievances != null &&
                (currentTime - lastFetchTime) < cacheValidityDuration
            ) {
                _personalGrievanceState.value =
                    PersonalGrievanceUiState.Success(cachedPersonalGrievances!!)
                return@launch
            }

            fetchPersonalGrievances()
        }
    }

    fun getAllNonPersonalGrievances(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()

            // Check if we can use cached data
            if (!forceRefresh &&
                cachedNonPersonalGrievances != null &&
                (currentTime - lastFetchTime) < cacheValidityDuration
            ) {
                _nonPersonalGrievances.value =
                    NonPersonalGrievanceUiState.Success(cachedNonPersonalGrievances!!)
                return@launch
            }

            fetchNonPersonalGrievances()
        }
    }

    private suspend fun getNonPersonalGrievances(forceRefresh: Boolean = false) {
        _nonPersonalGrievances.value = NonPersonalGrievanceUiState.Loading

        try {
            val result = grievanceRepository.getAllNonPersonalGrievances()

            result.fold(
                onSuccess = { response ->
                    Log.d("GrievanceViewModel", "Network response: $response")

                    val successResponse = ArrayList<GroupGrievanceResponse>()

                    if (response[0].statusCode == 200) {
                        for (res in response) {
                            val grievance = GroupGrievanceResponse(
                                grievanceResponse = res.grievanceResponse,
                                userResponse = res.userResponse,
                                statusCode = res.statusCode
                            )
                            successResponse.add(grievance)
                        }
                        // Update cache
                        cachedNonPersonalGrievances = successResponse
                        lastFetchTime = System.currentTimeMillis()

                        _nonPersonalGrievances.value =
                            NonPersonalGrievanceUiState.Success(successResponse)
                    } else {
                        _nonPersonalGrievances.value = NonPersonalGrievanceUiState.Error(
                            ErrorResponse(
                                "Failed to fetch grievances: ${response[0].statusCode}",
                                null,
                                response[0].statusCode
                            )
                        )
                    }
                },
                onFailure = { throwable ->
                    val error = when {
                        throwable.message?.contains("Malformed") == true -> {
                            ErrorResponse(
                                "Something went wrong. Please try again later.",
                                null,
                                500
                            )
                        }

                        throwable.message?.contains("timeout") == true -> {
                            ErrorResponse(
                                "Request timeout. Please check your connection.",
                                null,
                                408
                            )
                        }

                        else -> {
                            ErrorResponse(throwable.message ?: "Unknown error", null, 400)
                        }
                    }
                    _nonPersonalGrievances.value = NonPersonalGrievanceUiState.Error(error)
                }
            )
        } catch (e: Exception) {
            _nonPersonalGrievances.value = NonPersonalGrievanceUiState.Error(
                ErrorResponse("Unexpected error occurred", null, 500)
            )
        }
    }

    /**
     * Force refresh grievances from network
     */
    suspend fun refreshGrievances() {
        getAllPersonalGrievances(forceRefresh = true)
        getNonPersonalGrievances(forceRefresh = true)
    }

    private suspend fun fetchPersonalGrievances() {
        _personalGrievanceState.value = PersonalGrievanceUiState.Loading

        try {
            val result = grievanceRepository.getAllPersonalGrievances()

            result.fold(
                onSuccess = { response ->

                    if (response.status == 200) {
                        val successResponse = UserGrievanceResponse(
                            grievance = response.grievance,
                            status = response.status
                        )

                        // Update cache
                        cachedPersonalGrievances = successResponse
                        lastFetchTime = System.currentTimeMillis()

                        _personalGrievanceState.value = PersonalGrievanceUiState.Success(successResponse)
                    } else {
                        _personalGrievanceState.value = PersonalGrievanceUiState.Error(
                            ErrorResponse(
                                "Failed to fetch grievances: ${response.status}",
                                null,
                                response.status
                            )
                        )
                    }
                },
                onFailure = { throwable ->
                    val error = when {
                        throwable.message?.contains("Malformed") == true -> {
                            ErrorResponse(
                                "Something went wrong. Please try again later.",
                                null,
                                500
                            )
                        }

                        throwable.message?.contains("timeout") == true -> {
                            ErrorResponse(
                                "Request timeout. Please check your connection.",
                                null,
                                408
                            )
                        }

                        else -> {
                            ErrorResponse(throwable.message ?: "Unknown error", null, 400)
                        }
                    }
                    _personalGrievanceState.value = PersonalGrievanceUiState.Error(error)
                }
            )
        } catch (e: Exception) {
            _personalGrievanceState.value = PersonalGrievanceUiState.Error(
                ErrorResponse("Unexpected error occurred", null, 500)
            )
        }
    }

    private suspend fun fetchNonPersonalGrievances() {
        _nonPersonalGrievances.value = NonPersonalGrievanceUiState.Loading

        try {
            val result = grievanceRepository.getAllNonPersonalGrievances()

            println("Result non personal grievances: $result")

            result.fold(
                onSuccess = { response ->
                    Log.d("GrievanceViewModel", "Network response: $response")

                    val successResponse = ArrayList<GroupGrievanceResponse>()

                    if (response[0].statusCode == 200) {
                        for (res in response) {
                            val grievance = GroupGrievanceResponse(
                                grievanceResponse = res.grievanceResponse,
                                userResponse = res.userResponse,
                                statusCode = res.statusCode
                            )
                            successResponse.add(grievance)
                        }
                        // Update cache
                        cachedNonPersonalGrievances = successResponse
                        lastFetchTime = System.currentTimeMillis()

                        _nonPersonalGrievances.value =
                            NonPersonalGrievanceUiState.Success(successResponse)
                    } else {
                        _nonPersonalGrievances.value = NonPersonalGrievanceUiState.Error(
                            ErrorResponse(
                                "Failed to fetch grievances: ${response[0].statusCode}",
                                null,
                                response[0].statusCode
                            )
                        )
                    }
                },
                onFailure = { throwable ->
                    val error = when {
                        throwable.message?.contains("Malformed") == true -> {
                            ErrorResponse(
                                "Something went wrong. Please try again later.",
                                null,
                                500
                            )
                        }

                        throwable.message?.contains("timeout") == true -> {
                            ErrorResponse(
                                "Request timeout. Please check your connection.",
                                null,
                                408
                            )
                        }

                        else -> {
                            ErrorResponse(throwable.message ?: "Unknown error", null, 400)
                        }
                    }
                    _nonPersonalGrievances.value = NonPersonalGrievanceUiState.Error(error)
                }
            )
        } catch (e: Exception) {
            _nonPersonalGrievances.value = NonPersonalGrievanceUiState.Error(
                ErrorResponse("Unexpected error occurred", null, 500)
            )
        }
    }

    fun createGrievance(grievanceRequest: GrievanceRequest) {
        _createGrievanceState.value = CreateGrievanceUiState.Loading

        viewModelScope.launch {
            val grievance = grievanceRepository.createGrievance(grievanceRequest)

            grievance.fold(
                onSuccess = { response ->
                    _createGrievanceState.value = CreateGrievanceUiState.Success(
                        response = response
                    )
                },
                onFailure = { throwable ->
                    val error = if (throwable.message?.contains("Malformed") == true) {
                        ErrorResponse("Something went wrong. Please try again later.", null, 500)
                    } else {
                        ErrorResponse(throwable.message ?: "Unknown error", null, 400)
                    }

                    _createGrievanceState.value = CreateGrievanceUiState.Error(error)
                }
            )
        }
    }

    /**
     * Clear cached data (useful when user logs out or switches accounts)
     */
    fun clearCache() {
        cachedPersonalGrievances = null
        lastFetchTime = 0L
    }

    /**
     * Check if data is cached and valid
     */
    fun isCacheValid(): Boolean {
        val currentTime = System.currentTimeMillis()
        return cachedPersonalGrievances != null &&
                (currentTime - lastFetchTime) < cacheValidityDuration
    }

    /**
     * Get cached grievances count (useful for UI)
     */
    fun getCachedGrievancesCount(): Int {
        return cachedPersonalGrievances?.grievance?.size ?: 0
    }

    sealed class GrievanceUiEvent {
        data object Navigate : GrievanceUiEvent()
        data object RefreshRequested : GrievanceUiEvent()
    }

    sealed class PersonalGrievanceUiState {
        data object Loading : PersonalGrievanceUiState()
        data class Success(val response: UserGrievanceResponse) : PersonalGrievanceUiState()
        data class Error(val error: ErrorResponse) : PersonalGrievanceUiState()
        data object Idle : PersonalGrievanceUiState()
    }

    sealed class NonPersonalGrievanceUiState {
        data object Loading : NonPersonalGrievanceUiState()
        data class Success(val response: List<GroupGrievanceResponse>) :
            NonPersonalGrievanceUiState()

        data class Error(val error: ErrorResponse) : NonPersonalGrievanceUiState()
        data object Idle : NonPersonalGrievanceUiState()
    }

    sealed class CreateGrievanceUiState {
        data object Loading : CreateGrievanceUiState()
        data class Success(val response: Grievance) : CreateGrievanceUiState()
        data class Error(val error: ErrorResponse) : CreateGrievanceUiState()
        data object Idle : CreateGrievanceUiState()
    }

    fun resetCreateGrievanceState() {
        _createGrievanceState.value = GrievanceViewModel.CreateGrievanceUiState.Idle
    }

}