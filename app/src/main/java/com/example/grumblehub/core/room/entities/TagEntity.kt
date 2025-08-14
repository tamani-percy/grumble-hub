package com.example.grumblehub.core.room.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tags",
    indices = [Index(value = ["name"], unique = true)]
)
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val tagId: Long,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null
)