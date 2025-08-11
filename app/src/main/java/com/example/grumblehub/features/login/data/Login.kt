package com.example.grumblehub.features.login.data

import com.google.gson.annotations.SerializedName


data class LoginRequest(
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String,
)

data class LoginResponse(
    @SerializedName("token") val token: String?,
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("authUserResponse") val authUserResponse: AuthUserResponse?

)

data class VerifyLoginOtpRequest(
    @SerializedName("email") val email: String,
    @SerializedName("otp") val otp: String,
)

data class JwtResponse(
    val issuer: String?,
    val claim: Map<String, String?>,
    val isExpired: Boolean,
    val subject: String?
)


data class AuthUserResponse(
    @SerializedName("email") val email: String,
    @SerializedName("userId") val userId: Long,
)

// Auth state enum
sealed class AuthState {
    data object Loading : AuthState()
    data object Authenticated : AuthState()
    data object Unauthenticated : AuthState()
}
