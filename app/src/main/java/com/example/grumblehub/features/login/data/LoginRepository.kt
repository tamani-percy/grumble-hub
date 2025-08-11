package com.example.grumblehub.features.login.data

import com.example.grumblehub.core.apis.AuthService
import com.example.grumblehub.sharedviewmodels.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException

class LoginRepository(private val authService: AuthService) {

    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        return try {
            val response = authService.login(loginRequest)
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


    suspend fun verifyLoginOtp(verifyLoginOtpRequest: VerifyLoginOtpRequest): Result<LoginResponse> {
        return try {
            val response = authService.verifyLoginOtp(verifyLoginOtpRequest)
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