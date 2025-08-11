package com.example.grumblehub

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import com.example.grumblehub.base.AppEntryPoint
import com.example.grumblehub.core.datastore.DataStoreManager
import com.example.grumblehub.features.grievance.data.GrievanceViewModel
import com.example.grumblehub.ui.theme.GrumbleHubTheme


class MainActivity : ComponentActivity() {
    private lateinit var dataStoreManager: DataStoreManager
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .crossfade(true)
                .build()
        }
        enableEdgeToEdge()
        dataStoreManager = DataStoreManager.getInstance(applicationContext)

        setContent {
            GrumbleHubTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppEntryPoint(
                        modifier = Modifier.padding(innerPadding),
                        dataStoreManager = dataStoreManager
                    )
                }
            }
        }
    }
}

