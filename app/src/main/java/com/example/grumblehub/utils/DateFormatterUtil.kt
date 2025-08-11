package com.example.grumblehub.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun convertAndFormatStringToLocalDateTime(
    dateTimeString: String,
    inputFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
): String? {
    return try {
        // Define the formatter for parsing the input string
        val parser = DateTimeFormatter.ofPattern(inputFormat)

        // Parse the string to LocalDateTime
        val localDateTime = LocalDateTime.parse(dateTimeString, parser)

        // Define the formatter for the desired output
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

        // Format the LocalDateTime to the desired string format
        localDateTime.format(outputFormatter)
    } catch (e: DateTimeParseException) {
        // Handle invalid date-time strings
        println("Error parsing date-time string: $dateTimeString. Reason: ${e.message}")
        null // Return null for invalid input
    }
}
