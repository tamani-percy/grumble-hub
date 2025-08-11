package com.example.grumblehub.features.signup.data

import android.util.Log
import com.example.grumblehub.core.apis.AuthService
import com.example.grumblehub.sharedviewmodels.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException

class SignupRepository(private val authService: AuthService) {

    suspend fun signup(signupRequest: SignupRequest): Result<SignupResponse> {
        return try {
            val response = authService.signup(signupRequest) // Retrofit API call
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

    suspend fun sendSignupOtp(signupRequest: SignupRequest): Result<OtpResponse> {
        return try {
            val response = authService.sendSignupOtp(signupRequest) // Retrofit API call
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


    suspend fun verifySignupOtp(verifySignupOtpRequest: VerifySignupOtpRequest): Result<OtpResponse> {
        return try {
            val response = authService.verifySignupOtp(verifySignupOtpRequest) // Retrofit API call
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

    suspend fun createPassword(createPasswordRequest: CreatePasswordRequest): Result<CreatePasswordResponse> {
        return try {
            val response = authService.createPassword(createPasswordRequest) // Retrofit API call
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