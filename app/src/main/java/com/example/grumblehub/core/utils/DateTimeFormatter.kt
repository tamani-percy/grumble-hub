package com.example.grumblehub.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun formatEpochMillis(millis: Long): String {
    val fmt = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm") }
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).format(fmt)
}