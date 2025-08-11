package com.example.grumblehub.features.grievance.data

import com.google.gson.annotations.SerializedName

data class TagResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("createdAt") val createdAt: String,
)