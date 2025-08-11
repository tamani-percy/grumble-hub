package com.example.grumblehub.core.datastore

import android.annotation.SuppressLint
import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.auth0.android.jwt.JWT
import com.example.grumblehub.features.login.data.JwtResponse
import kotlinx.coroutines.flow.first

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("grumble_hub_secure_data_store")

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

        private const val JWT_KEY_ALIAS = "jwt_encryption_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"

        private val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val JWT_IV_KEY = stringPreferencesKey("jwt_token_iv")
        private val USER_ID = longPreferencesKey("user_id")
        private val GROUP_ID = longPreferencesKey("group_id")
        private val GRUMBLER_ID = longPreferencesKey("grumbler_id")
        private val EMAIL = stringPreferencesKey("email")
    }

    suspend fun setEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[EMAIL] = email;
        }
    }

    suspend fun setGrumblerId(grumblerId: Long) {
        context.dataStore.edit { preferences ->
            preferences[GRUMBLER_ID] = grumblerId;
        }
    }

    suspend fun setUserId(userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId;
        }
    }

    suspend fun setGroupId(groupId: Long) {
        context.dataStore.edit { preferences ->
            preferences[GROUP_ID] = groupId;
        }
    }

    fun getEmail(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[EMAIL]
        }
    }

    fun getGrumblerId(): Flow<Long?> {
        return context.dataStore.data.map { preferences ->
            preferences[GRUMBLER_ID]
        }
    }

    fun getUserId(): Flow<Long?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID]
        }
    }

    fun getGroupId(): Flow<Long?> {
        return context.dataStore.data.map { preferences ->
            preferences[GROUP_ID]
        }
    }


    // Generate or retrieve encryption key from Android Keystore
    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val existingKey = keyStore.getEntry(JWT_KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        if (existingKey != null) return existingKey.secretKey

        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenSpec = KeyGenParameterSpec.Builder(
            JWT_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(false)
            .build()
        keyGenerator.init(keyGenSpec)
        return keyGenerator.generateKey()
    }

    private fun encrypt(token: String, secretKey: SecretKey): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val encrypted = cipher.doFinal(token.toByteArray(Charsets.UTF_8))
        return Pair(encrypted, iv)
    }

    private fun decrypt(encrypted: ByteArray, iv: ByteArray, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
        val decoded = cipher.doFinal(encrypted)
        return String(decoded, Charsets.UTF_8)
    }

    suspend fun saveJwtTokenSecure(token: String) {
        try {
            val key = getOrCreateSecretKey()

            val (encryptedBytes, ivBytes) = encrypt(token, key)

            context.dataStore.edit { preferences ->
                preferences.clear()
                preferences[JWT_TOKEN_KEY] = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
                preferences[JWT_IV_KEY] = Base64.encodeToString(ivBytes, Base64.DEFAULT)
            }

        } catch (e: Exception) {
            throw e
        }
    }

    fun readJwtTokenSecure(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            val encBase64 = preferences[JWT_TOKEN_KEY]
            val ivBase64 = preferences[JWT_IV_KEY]

            if (encBase64 != null && ivBase64 != null) {
                try {
                    val key = getOrCreateSecretKey()
                    val encrypted = Base64.decode(encBase64, Base64.DEFAULT)
                    val iv = Base64.decode(ivBase64, Base64.DEFAULT)
                    decrypt(encrypted, iv, key)
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        }
    }

    private suspend fun clearJwtToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(JWT_TOKEN_KEY)
            preferences.remove(JWT_IV_KEY)
        }
    }

    private suspend fun decodeJwtToken(): JwtResponse {
        val token = readJwtTokenSecure()
        val jwt = token.first()?.let { JWT(it) }

        if (jwt == null) {
            return JwtResponse(
                issuer = null,
                claim = emptyMap(),
                isExpired = true,
                subject = null
            )
        }

        val issuer = jwt.issuer
        val subject = jwt.subject
        val isExpired = jwt.isExpired(10)
        // Extract specific claims
        val role = jwt.getClaim("role").asString()
        val issuedAt = jwt.issuedAt?.time?.toString()
        val expiresAt = jwt.expiresAt?.time?.toString()

        // Put the decoded claims in a readable map
        val claimMap = mapOf(
            "role" to role,
            "iat" to issuedAt,
            "exp" to expiresAt,
            "sub" to subject
        )

        return JwtResponse(
            issuer = issuer,
            claim = claimMap,
            isExpired = isExpired,
            subject = subject
        )
    }


    // Add this extension function to your DataStoreManager.kt file:
    suspend fun isUserAuthenticated(): Boolean {
        return try {
            val jwtResponse = decodeJwtToken()
            jwtResponse.subject != null && !jwtResponse.isExpired
        } catch (e: Exception) {
            false
        }
    }

    // Extension function for logout
    suspend fun logout() {
        clearJwtToken()
        // Clear other user data if needed
        setUserId(0)
        setGrumblerId(0)
        setEmail("")
    }

}
