package com.example.grumblehub.features.signup.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grumblehub.sharedviewmodels.ErrorResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SignupViewModel(private val signupRepository: SignupRepository) : ViewModel() {

    private val _signupEvents = MutableSharedFlow<SignupEvent>()
    val signupEvents = _signupEvents.asSharedFlow()

    private val _signupState = MutableLiveData<SignupUiState>()
    val signupState: LiveData<SignupUiState> = _signupState

    private val _otpState = MutableLiveData<SignupUiState>()
    private val otpState: LiveData<SignupUiState> = _otpState

    private val _passwordState = MutableLiveData<NewPasswordUiState>()
    private val passwordState: LiveData<NewPasswordUiState> = _passwordState

    private val _newPasswordState = MutableLiveData<NewPasswordUiState>()
    val newPasswordState: LiveData<NewPasswordUiState> = _newPasswordState


    fun signupAndSendOtp(signupRequest: SignupRequest) {
        _signupState.value = SignupUiState.Loading
        _otpState.value = SignupUiState.Loading

        viewModelScope.launch {
            val signupResult = signupRepository.signup(signupRequest)

            signupResult.fold(
                onSuccess = { signupResponse ->
                    if (signupResponse.status == 200) {
                        val otpResult = signupRepository.sendSignupOtp(signupRequest)

                        otpResult.fold(
                            onSuccess = { otpResponse ->
                                _otpState.value = SignupUiState.Success(otpResponse.message)

                                _signupEvents.emit(SignupEvent.NavigateToDashboard)
                            },
                            onFailure = { throwable ->
                                val error = ErrorResponse(
                                    throwable.message ?: "OTP send failed", null, 400
                                )
                                _otpState.value = SignupUiState.Error(error)
                            }
                        )
                    } else {
                        _signupState.value = SignupUiState.Error(
                            ErrorResponse(
                                "Signup failed with status ${signupResponse.status}",
                                null,
                                signupResponse.status
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

                    _signupState.value = SignupUiState.Error(error)
                }
            )
        }
    }

    fun verifySignupOtp(verifySignupOtpRequest: VerifySignupOtpRequest) {
        _otpState.value = SignupUiState.Loading

        viewModelScope.launch {
            val verifySignupOtpResponse = signupRepository.verifySignupOtp(verifySignupOtpRequest)

            verifySignupOtpResponse.fold(
                onSuccess = { verifyOtpResponse ->
                    if (verifyOtpResponse.message.isNotEmpty()) {
                        _otpState.value = SignupUiState.Success(verifyOtpResponse.message)
                        _signupEvents.emit((SignupEvent.NavigateToDashboard))
                    } else {
                        _signupState.value = SignupUiState.Error(
                            ErrorResponse(
                                "Signup failed with status ${verifyOtpResponse.message}",
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

                    _signupState.value = SignupUiState.Error(error)
                }
            )
        }
    }


    fun createPassword(createPasswordRequest: CreatePasswordRequest) {
        _passwordState.value = NewPasswordUiState.Loading

        viewModelScope.launch {
            val createPasswordResponse = signupRepository.createPassword(createPasswordRequest)

            createPasswordResponse.fold(
                onSuccess = { response ->
                    if (response.message.isNotEmpty()) {
                        _passwordState.value = NewPasswordUiState.Success(
                            CreatePasswordResponse(
                                token = response.token,
                                message = response.message
                            )
                        )
                        _signupEvents.emit((SignupEvent.NavigateToDashboard))
                    } else {
                        _passwordState.value = NewPasswordUiState.Error(
                            ErrorResponse(
                                "Signup failed with status ${response.message}",
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

                    _passwordState.value = NewPasswordUiState.Error(error)
                }
            )
        }
    }

    sealed class SignupUiState {
        data object Loading : SignupUiState()
        data class Success(val message: String) : SignupUiState()
        data class Error(val error: ErrorResponse) : SignupUiState()
        data object Idle : SignupUiState()
    }

    sealed class NewPasswordUiState {
        data object Loading : NewPasswordUiState()
        data class Success(val response: CreatePasswordResponse) : NewPasswordUiState()
        data class Error(val error: ErrorResponse) : NewPasswordUiState()
        data object Idle : NewPasswordUiState()
    }


    sealed class SignupEvent {
        data object NavigateToDashboard : SignupEvent()
    }
}