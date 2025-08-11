package com.example.grumblehub.features.login.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grumblehub.sharedviewmodels.ErrorResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginEvents = MutableSharedFlow<LoginUiEvent>()
    val loginEvents = _loginEvents.asSharedFlow()

    private val _loginState = MutableLiveData<LoginUiState>()
    val loginState: LiveData<LoginUiState> = _loginState

    private val _otpState = MutableLiveData<LoginUiState>()
    val otpState: LiveData<LoginUiState> = _otpState

    fun login(loginRequest: LoginRequest) {
        _loginState.value = LoginUiState.Loading

        viewModelScope.launch {
            val login = loginRepository.login(loginRequest)

            login.fold(
                onSuccess = { response ->
                    if (response.status == 202) {
                        _loginState.value = LoginUiState.Success(
                            LoginResponse(
                                token = response.token,
                                message = response.message,
                                status = response.status,
                                authUserResponse = null
                            )
                        )
                        _loginEvents.emit((LoginUiEvent.Navigate))
                    } else {
                        _loginState.value = LoginUiState.Error(
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

                    _loginState.value = LoginUiState.Error(error)
                }
            )
        }
    }

    fun verifyLoginOtp(verifyLoginOtpRequest: VerifyLoginOtpRequest) {
        _otpState.value = LoginUiState.Loading

        viewModelScope.launch {
            val login = loginRepository.verifyLoginOtp(verifyLoginOtpRequest)

            login.fold(
                onSuccess = { response ->
                    if (response.status == 200) {
                        _otpState.value = LoginUiState.Success(
                            LoginResponse(
                                token = response.token,
                                message = response.message,
                                status = response.status,
                                authUserResponse = response.authUserResponse?.let {
                                    AuthUserResponse(
                                        email = it.email,
                                        userId = response.authUserResponse.userId
                                    )
                                }
                            )
                        )
                        _loginEvents.emit((LoginUiEvent.Navigate))
                    } else {
                        _otpState.value = LoginUiState.Error(
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

                    _otpState.value = LoginUiState.Error(error)
                }
            )
        }
    }

    sealed class LoginUiState {
        data object Loading : LoginUiState()
        data class Success(val response: LoginResponse) : LoginUiState()
        data class Error(val error: ErrorResponse) : LoginUiState()
        data object Idle : LoginUiState()
    }


    sealed class LoginUiEvent {
        data object Navigate : LoginUiEvent()
    }

}