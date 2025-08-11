package com.example.grumblehub.features.signup.data

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String = "ROLE_USER"
)

data class SignupResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String,
    @SerializedName("status") val status: Int
)

data class OtpResponse(
    @SerializedName("message") val message: String
)

data class VerifySignupOtpRequest(
    @SerializedName("email") val email: String,
    @SerializedName("otp") val otp: String, )


data class CreatePasswordRequest(
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String, )

data class CreatePasswordResponse(
    @SerializedName("token") val token: String,
    @SerializedName("message") val message: String, )
