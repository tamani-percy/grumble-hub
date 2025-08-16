package com.example.grumblehub.features.home.data

import androidx.room.Embedded
import com.example.grumblehub.core.room.entities.GrievanceEntity
import com.example.grumblehub.core.room.entities.MoodEntity
import com.example.grumblehub.core.room.entities.TagEntity

data class GrievanceDto(
    val title: String,
    val grievance: String,
    val moodId: Long,
    val tagId: Long,
)

data class Grievance(
    @Embedded val grievance: GrievanceEntity,
    @Embedded(prefix = "m_") val mood: MoodEntity,
    @Embedded(prefix = "t_") val tag: TagEntity
)
