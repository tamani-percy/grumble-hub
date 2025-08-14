package com.example.grumblehub.features.home.data

data class GrievanceDto(
    val grievanceId: Long? = null,
    val title: String,
    val grievance: String,
    val moodId: Long,
    val tagId: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null
)