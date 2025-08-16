package com.example.grumblehub.core.utils

import com.example.grumblehub.R
import com.example.grumblehub.core.room.entities.MoodEntity


fun grievanceMoodGifs(mood: MoodEntity): Int = when (mood.name) {
    "Happy" -> R.drawable.happy
    "Upset" -> R.drawable.upset
    "Disgust" -> R.drawable.disgust
    "Sad" -> R.drawable.sad
    "Neutral" -> R.drawable.neutral
    else -> R.drawable.happy
}