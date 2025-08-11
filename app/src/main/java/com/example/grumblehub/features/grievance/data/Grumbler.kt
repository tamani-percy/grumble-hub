package com.example.grumblehub.features.grievance.data

import com.google.gson.annotations.SerializedName

data class GrumblerResponse(
    @SerializedName("grievanceResponse") val id:Long,
    @SerializedName("grievanceResponse") val grumblerId:Long,
    @SerializedName("status") val status: String,
    @SerializedName("joinedAt") val joinedAt: String,
    @SerializedName("userResponse") val userResponse: UserResponse

    )

data class UserResponse (
    @SerializedName("email") val email: String

)