package com.example.grumblehub.features.grievance.data

import com.google.gson.annotations.SerializedName

data class Grievance(
    @SerializedName("id") val id: Long,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("isPersonal") val isPersonal: Boolean,
    @SerializedName("isRead") val isRead: Boolean,
    @SerializedName("title") val title: String,
    @SerializedName("grievance") val grievance: String,
    @SerializedName("mood") val mood: MoodResponse,
    @SerializedName("tag") val tag: TagResponse
)

data class UserGrievanceResponse(
    @SerializedName("grievanceResponse") val grievance: List<Grievance>,
    @SerializedName("status") val status: Int
)

data class UserGrievanceRequest(
    @SerializedName("userId") val userId: Long
)


data class GroupGrievanceResponse(
    @SerializedName("grievanceResponse") val grievanceResponse: List<Grievance>,
    @SerializedName("userResponse") val userResponse: UserGrumblerResponse,
    @SerializedName("statusCode") val statusCode:Int
)

data class GrievanceRequest(
    @SerializedName("userId") val userId: Long,
    @SerializedName("isPersonal") val isPersonal: Boolean,
    @SerializedName("title") val title: String,
    @SerializedName("grievance") val grievance: String,
    @SerializedName("moodId") val moodId: Long,
    @SerializedName("tagId") val tagId: Long,
)

data class UserGrumblerResponse(
    @SerializedName("userId") val userId: Long,
    @SerializedName("groupId") val groupId: Long,
    @SerializedName("grumblerId") val grumblerId: Long,
    @SerializedName("grumblerJoinedAt") val grumblerJoinedAt: String,
    @SerializedName("email") val email: String,

    )


data class GroupGrievanceRequest(
    @SerializedName("groupId") val groupId:Long
)