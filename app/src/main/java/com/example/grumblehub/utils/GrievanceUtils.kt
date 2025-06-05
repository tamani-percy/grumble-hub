package com.example.grumblehub.utils

import com.example.grumblehub.R
import com.example.grumblehub.features.grievance.data.Mood
import com.example.grumblehub.features.grievance.data.Tag

fun grievanceImage(tag: Tag): Int = when (tag.tag) {
    "Work" -> R.drawable.work
    "Relationship" -> R.drawable.relationship
    "Home" -> R.drawable.home
    "Social" -> R.drawable.social
    else -> R.drawable.work
}

fun grievanceMoodGifs(mood: Mood): Int = when (mood.mood) {
    "Happy" -> R.drawable.happy
    "Upset" -> R.drawable.upset
    "Disgust" -> R.drawable.disgust
    "Sad" -> R.drawable.sad
    "Neutral" -> R.drawable.neutral
    else -> R.drawable.happy
}