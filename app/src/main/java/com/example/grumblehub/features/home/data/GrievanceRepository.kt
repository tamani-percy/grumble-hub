package com.example.grumblehub.features.home.data

import com.example.grumblehub.core.room.dao.GrievanceDao
import com.example.grumblehub.core.room.entities.GrievanceEntity

class GrievanceRepository(
    private val grievanceDao: GrievanceDao,
) {

    suspend fun getGrievances(): List<GrievanceEntity> {
        return grievanceDao.getAllGrievances()
    }

    suspend fun createGrievance(grievance: GrievanceDto): Result<GrievanceEntity> {
        return try {
            val grievanceEntity = GrievanceEntity(
                grievanceId = grievance.grievanceId ?: 0,
                title = grievance.title,
                grievance = grievance.grievance,
                moodId = grievance.moodId,
                tagId = grievance.tagId,
                createdAt = grievance.createdAt,
                updatedAt = grievance.updatedAt
            )
            grievanceDao.insertGrievance(grievanceEntity)
            Result.success(grievanceEntity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteGrievance(grievance: GrievanceEntity) {
        grievanceDao.deleteGrievance(grievance)
    }

//    suspend fun updateGrievance(grievance: GrievanceEntity) {
//        grievanceDao.updateGrievance(grievance)
//    }

}