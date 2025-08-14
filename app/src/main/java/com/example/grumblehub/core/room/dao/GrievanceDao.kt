package com.example.grumblehub.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grumblehub.core.room.entities.GrievanceEntity

@Dao
interface GrievanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrievance(grievance: GrievanceEntity)

    @Query("SELECT * FROM grievances WHERE grievanceId = :grievanceId")
    suspend fun updateGrievance(grievanceId: Long): GrievanceEntity?

    @Query("SELECT * FROM grievances WHERE grievanceId = :grievanceId")
    suspend fun getGrievanceById(grievanceId: Long): GrievanceEntity?

    @Query("SELECT * FROM grievances")
    suspend fun getAllGrievances(): List<GrievanceEntity>

    @Delete
    suspend fun deleteGrievance(grievance: GrievanceEntity)
}