package com.example.grumblehub.features.grievance.data

import java.time.LocalDate
import java.util.Date

data class Grievance(
    val id: Int,
    val tag: Tag,
    val mood: Mood,
    val title: String,
    val grievance: String,
    val isRead: Boolean,
    val dateCreated: LocalDate,
    val isPersonal:Boolean
)


data class Mood(
    val id: Int,
    val mood: String,
    val dateCreated: LocalDate
)

data class Tag(
    val id: Int,
    val tag: String,
    val dateCreated: LocalDate
)