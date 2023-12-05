package at.luki0606.beerney.models

import android.content.Context

object IpAddress {
    fun getIpAddress(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("Beerney", Context.MODE_PRIVATE)
        return sharedPreferences.getString("IP_ADDRESS", null) ?: ""
    }

    fun setIpAddress(ipAddress: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences("Beerney", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("IP_ADDRESS", ipAddress)
        editor.apply()
    }
}