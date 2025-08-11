package com.example.grumblehub.core.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "grievances",
    foreignKeys = [
        ForeignKey(
            entity = MoodEntity::class,
            parentColumns = ["moodId"],
            childColumns = ["moodId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["tagId"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tagId")]
)
data class GrievanceEntity(
    @PrimaryKey(autoGenerate = true)
    val grievanceId: String,
    val title: String,
    val grievance: String,
    val moodId: Long,
    val tagId: Long,
    val isRead: Boolean,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null
)