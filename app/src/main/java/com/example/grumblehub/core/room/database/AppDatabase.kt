package com.example.grumblehub.core.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.grumblehub.core.room.dao.GrievanceDao
import com.example.grumblehub.core.room.dao.MoodDao
import com.example.grumblehub.core.room.dao.TagDao
import com.example.grumblehub.core.room.entities.GrievanceEntity
import com.example.grumblehub.core.room.entities.MoodEntity
import com.example.grumblehub.core.room.entities.TagEntity

@Database(entities = [GrievanceEntity::class, MoodEntity::class, TagEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun grievanceDao(): GrievanceDao

    abstract fun moodDao(): MoodDao

    abstract fun tagDao(): TagDao
}