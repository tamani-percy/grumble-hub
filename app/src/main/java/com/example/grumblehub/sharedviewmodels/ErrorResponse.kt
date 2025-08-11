package com.example.grumblehub.sharedviewmodels

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ErrorResponse(
    @SerializedName("error") val error: String,
    @SerializedName("timestamp") val timestamp: String?,
    @SerializedName("status") val status: Int
)
