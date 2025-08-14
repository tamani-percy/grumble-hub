package com.example.grumblehub.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grumblehub.core.room.entities.MoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao{

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertMood(mood: MoodEntity)

     @Query("SELECT * FROM moods WHERE moodId = :moodId")
     suspend fun getMoodById(moodId: Long): MoodEntity?

     @Query("SELECT * FROM moods ORDER BY name")
     suspend fun getAllMoods(): List<MoodEntity>

     @Query("SELECT * FROM moods ORDER BY name")
     fun observeAllMoods(): Flow<List<MoodEntity>>

     @Delete
     suspend fun deleteMood(mood: MoodEntity)
}