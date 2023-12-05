package at.luki0606.beerney.models

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object IpAddress {

    fun getIpAddress(context: Context): String {
        val masterKey = getMasterKey(context)
        val secureSharedPrefs = getSecureSharedPrefs(masterKey, context)
        return secureSharedPrefs.getString("IP_ADDRESS", null) ?: ""
    }

    fun setIpAddress(ipAddress: String, context: Context) {
        val masterKey = getMasterKey(context)
        val secureSharedPrefs = getSecureSharedPrefs(masterKey, context)

        val editor = secureSharedPrefs.edit()
        editor.putString("IP_ADDRESS", ipAddress)
        editor.apply()
    }

    private fun getSecureSharedPrefs(masterKey: MasterKey, context: Context): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            "Beerney",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun getMasterKey(context: Context): MasterKey {
        return MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }
}