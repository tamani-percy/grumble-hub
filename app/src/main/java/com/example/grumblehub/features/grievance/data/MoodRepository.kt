package com.example.grumblehub.features.grievance.data

import com.example.grumblehub.core.apis.MoodService
import com.example.grumblehub.sharedviewmodels.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException

class MoodRepository(private val moodService: MoodService) {


    suspend fun getAllMoods(): Result<List<MoodResponse>> {
        return try {
            val response = moodService.getAllMoods()
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