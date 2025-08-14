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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.compose.GrumbleHubTheme
import com.example.grumblehub.core.MainAppScaffold
import com.example.grumblehub.core.datastore.DataStoreManager

class MainActivity : ComponentActivity() {
    private lateinit var dataStoreManager: DataStoreManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppEntryPoint(modifier: Modifier, dataStoreManager: DataStoreManager) {
    MainAppScaffold(dataStoreManager = dataStoreManager)
}