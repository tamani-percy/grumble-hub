package com.example.grumblehub.features.home.data

import com.example.grumblehub.core.room.dao.MoodDao
import com.example.grumblehub.core.room.entities.MoodEntity
import kotlinx.coroutines.flow.Flow

class MoodRepository(
    private val moodDao: MoodDao,
) {

    suspend fun getMoods(): Result<List<MoodEntity>> {
        return try {
            val moods = moodDao.getAllMoods()
            Result.success(moods)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun observeMoods(): Flow<List<MoodEntity>> = moodDao.observeAllMoods()

    suspend fun createMood(mood: MoodDto): Result<MoodEntity> {
        return try {
            val moodEntity = MoodEntity(
                name = mood.name,
                moodId = 0,
                createdAt = System.currentTimeMillis(),
                updatedAt = null,
            )
            moodDao.insertMood(moodEntity)
            Result.success(moodEntity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteMood(mood: MoodEntity) {
        moodDao.deleteMood(mood)
    }
}