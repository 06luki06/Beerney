package at.luki0606.beerney.models

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

data class BeerInfo(
    val manufacturer: String = Build.MANUFACTURER,
    val model: String = Build.MODEL,
    val androidVersion: String = Build.VERSION.RELEASE,
    val apiLevel: Int = Build.VERSION.SDK_INT,
    val display: String = Build.DISPLAY,
    val board: String = Build.BOARD,
    val bootloader: String = Build.BOOTLOADER,
    val brand: String = Build.BRAND,
    val device: String = Build.DEVICE,
    val fingerprint: String = Build.FINGERPRINT,
    val host: String = Build.HOST,
    val product: String = Build.PRODUCT,
    val user: String = Build.USER,
)

object BeerInfoSSPrefs{
    fun getBeerInfo(context: Context): Boolean {
        val masterKey = getMasterKey(context)
        val secureSharedPrefs = getSecureSharedPrefs(masterKey, context)
        return secureSharedPrefs.getBoolean("GOT_BEER_INFO", false)
    }

    fun setBeerInfo(context: Context) {
        val masterKey = getMasterKey(context)
        val secureSharedPrefs = getSecureSharedPrefs(masterKey, context)

        val editor = secureSharedPrefs.edit()
        editor.putBoolean("GOT_BEER_INFO", true)
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