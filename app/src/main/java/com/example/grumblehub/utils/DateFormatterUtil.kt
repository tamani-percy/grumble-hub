package com.example.grumblehub.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
fun formatLocalDateToReadableString(date: LocalDate): String {
    val day = date.dayOfMonth
    val suffix = getDayOfMonthSuffix(day)
    val month = date.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
    val year = date.year
    return "$day$suffix $month, $year"
}

private fun getDayOfMonthSuffix(day: Int): String {
    return if (day in 11..13) {
        "th"
    } else when (day % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}