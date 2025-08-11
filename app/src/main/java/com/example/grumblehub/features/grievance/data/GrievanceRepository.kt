package com.example.grumblehub.features.grievance.data

import com.example.grumblehub.core.apis.GrievanceService
import com.example.grumblehub.core.datastore.DataStoreManager
import com.example.grumblehub.sharedviewmodels.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

class GrievanceRepository(
    private val grievanceService: GrievanceService,
    private val dataStoreManager: DataStoreManager
) {

    suspend fun getAllPersonalGrievances(): Result<UserGrievanceResponse> {
        return try {
            val userId = dataStoreManager.getUserId().first()
            val userGrievanceRequest = UserGrievanceRequest(userId = userId?.toLong() ?: 0)
            val response = grievanceService.getAllPersonalGrievances(userGrievanceRequest)
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = try {
                errorBody?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                } ?: ErrorResponse("Unknown error", null, 500)
            } catch (ex: Exception) {
                ErrorResponse("Malformed error response", null, 500)
            }
            Result.failure(Exception(errorResponse.error))
        }
    }

    suspend fun getAllNonPersonalGrievances(): Result<List<GroupGrievanceResponse>> {
        return try {
            val groupId = dataStoreManager.getGroupId().first()
            val groupGrievanceRequest = GroupGrievanceRequest(groupId = groupId?.toLong() ?: 1)
            val response = grievanceService.getAllNonPersonalGrievances(groupGrievanceRequest)
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = try {
                errorBody?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                } ?: ErrorResponse("Unknown error", null, 500)
            } catch (ex: Exception) {
                ErrorResponse("Malformed error response", null, 500)
            }
            Result.failure(Exception(errorResponse.error))
        }
    }


    suspend fun createGrievance(grievanceRequest: GrievanceRequest): Result<Grievance> {
        return try {
            val response = grievanceService.createGrievance(grievanceRequest)
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = try {
                errorBody?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                } ?: ErrorResponse("Unknown error", null, 500)
            } catch (ex: Exception) {
                ErrorResponse("Malformed error response", null, 500)
            }
            Result.failure(Exception(errorResponse.error))
        }
    }
}