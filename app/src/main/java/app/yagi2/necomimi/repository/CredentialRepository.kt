package app.yagi2.necomimi.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class CredentialRepository(applicationContext: Context) {

    companion object {
        private const val KEY_ACCESS_KEY = "access_key"
        private const val KEY_SECRET_KEY = "secret_key"
    }

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val preferences = EncryptedSharedPreferences.create(
        "credential_shared_prefs",
        masterKeyAlias,
        applicationContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun setAccessKey(accessKey: CharSequence) {
        with(preferences.edit()) {
            putString(KEY_ACCESS_KEY, accessKey.toString())
            apply()
        }
    }

    fun getAccessKey(): CharSequence? {
        return preferences.getString(KEY_ACCESS_KEY, null)
    }

    fun setSecretKey(secretKey: CharSequence) {
        with(preferences.edit()) {
            putString(KEY_SECRET_KEY, secretKey.toString())
            apply()
        }
    }

    fun getSecretKey(): CharSequence? {
        return preferences.getString(KEY_SECRET_KEY, null)
    }
}