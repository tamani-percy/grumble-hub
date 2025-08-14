package com.example.grumblehub.core.datastore

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "grumble_hub_secure_data_store")

class DataStoreManager(private val context: Context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: DataStoreManager? = null

        fun getInstance(context: Context): DataStoreManager {
            return INSTANCE ?: synchronized(this) {
                val instance = DataStoreManager(context)
                INSTANCE = instance
                instance
            }
        }

        private val STEPPER = booleanPreferencesKey("stepper")
    }

    suspend fun setStepper(stepper: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[STEPPER] = stepper;
        }
    }


    fun getStepper(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[STEPPER] ?: false
        }
    }


}
