package com.example.grumblehub.features.home.data

import com.example.grumblehub.core.room.dao.GrievanceDao
import com.example.grumblehub.core.room.entities.GrievanceEntity
import kotlinx.coroutines.flow.Flow

class GrievanceRepository(
    private val grievanceDao: GrievanceDao,
) {
    fun observeGrievances(): Flow<List<Grievance>> = grievanceDao.observeAllGrievances()

    suspend fun createGrievance(grievance: GrievanceDto): Result<GrievanceEntity> {
        return try {
            val grievanceEntity = GrievanceEntity(
                grievanceId = 0,
                title = grievance.title,
                grievance = grievance.grievance,
                moodId = grievance.moodId,
                tagId = grievance.tagId,
            )
            grievanceDao.insertGrievance(grievanceEntity)
            Result.success(grievanceEntity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGrievanceById(grievanceId: Long): Result<Grievance?> {
        return try {
            val res = grievanceDao.getGrievanceById(grievanceId)
            Result.success(res)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteGrievanceById(grievanceId: Long): Result<Boolean> {
        return try {
            val res = grievanceDao.deleteGrievanceById(grievanceId)
            if (res > 0) {
                Result.success(true)
            } else {
                Result.success(false)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

//    suspend fun updateGrievance(grievance: GrievanceEntity) {
//        grievanceDao.updateGrievance(grievance)
//    }

}