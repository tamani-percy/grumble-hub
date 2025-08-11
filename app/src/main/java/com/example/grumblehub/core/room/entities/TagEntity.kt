package com.example.grumblehub.core.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tags",
)
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val tagId: Long,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null
)