package com.example.grumblehub.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grumblehub.core.room.entities.GrievanceEntity
import com.example.grumblehub.features.home.data.Grievance
import kotlinx.coroutines.flow.Flow

@Dao
interface GrievanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrievance(grievance: GrievanceEntity)

    @Query(
        """
        SELECT 
            g.*,
            m.moodId   AS m_moodId,   m.name   AS m_name,   m.createdAt AS m_createdAt, 
            t.tagId    AS t_tagId,    t.name   AS t_name,   t.createdAt AS t_createdAt 
        FROM grievances g
        JOIN moods m ON m.moodId = g.moodId
        JOIN tags  t ON t.tagId  = g.tagId
        WHERE grievanceId = :grievanceId
    """
    )
    suspend fun getGrievanceById(grievanceId: Long): Grievance?

    @Query(
        """
        SELECT 
            g.*,
            m.moodId   AS m_moodId,   m.name   AS m_name,   m.createdAt AS m_createdAt, 
            t.tagId    AS t_tagId,    t.name   AS t_name,   t.createdAt AS t_createdAt 
        FROM grievances g
        JOIN moods m ON m.moodId = g.moodId
        JOIN tags  t ON t.tagId  = g.tagId
        ORDER BY g.createdAt DESC
    """
    )
    fun observeAllGrievances(): Flow<List<Grievance>>

    @Query("DELETE FROM grievances WHERE grievanceId = :grievanceId ")
    suspend fun deleteGrievanceById(grievanceId: Long): Int
}

