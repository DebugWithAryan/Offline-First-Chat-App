package com.aryan.offlinefirstchatapp.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit

class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "chat_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveSession(userId: String, token: String){
        prefs.edit {
            putString(KEY_USER_ID, userId)
                .putString(KEY_TOKEN, token)
        }
    }

    fun getUserId(): String = prefs.getString(KEY_USER_ID, "") ?: ""

    fun getToken(): String = prefs.getString(KEY_TOKEN, "") ?: ""

    fun clearSession() {
        prefs.edit { clear() }
    }

    fun isLoggedIn(): Boolean = getToken().isNotEmpty()


    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_TOKEN = "token"
    }

}