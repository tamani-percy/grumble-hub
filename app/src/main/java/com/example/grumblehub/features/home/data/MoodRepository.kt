package com.example.grumblehub.features.home.data

import com.example.grumblehub.core.room.dao.MoodDao
import com.example.grumblehub.core.room.entities.MoodEntity

class MoodRepository(
    private val moodDao: MoodDao,
) {

    suspend fun getMoods(): List<MoodEntity> {
        return moodDao.getAllMoods()
    }

    suspend fun addMood(mood: MoodEntity): Result<MoodEntity> {
        return try {
            moodDao.insertMood(mood)
            Result.success(mood)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteMood(mood: MoodEntity) {
        moodDao.deleteMood(mood)
    }
}