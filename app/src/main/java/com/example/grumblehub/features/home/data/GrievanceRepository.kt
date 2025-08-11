package com.example.grumblehub.features.home.data

import com.example.grumblehub.core.room.dao.GrievanceDao
import com.example.grumblehub.core.room.entities.GrievanceEntity

class GrievanceRepository(
    private val grievanceDao: GrievanceDao,
) {

    suspend fun getGrievances(): List<GrievanceEntity> {
        return grievanceDao.getAllGrievances()
    }

    suspend fun addGrievance(grievance: GrievanceEntity): Result<GrievanceEntity> {
        return try {
            grievanceDao.insertGrievance(grievance)
            Result.success(grievance)
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