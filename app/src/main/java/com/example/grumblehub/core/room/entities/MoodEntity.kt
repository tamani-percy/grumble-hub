package com.example.grumblehub.core.room.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "moods",
    indices = [Index(value = ["name"], unique = true)]
)
data class MoodEntity(
    @PrimaryKey(autoGenerate = true)
    val moodId: Long,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null
)