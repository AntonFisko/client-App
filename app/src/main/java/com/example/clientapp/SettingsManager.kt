package com.example.clientapp

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("client_config", Context.MODE_PRIVATE)

    fun getIp(): String = prefs.getString("ip", "192.168.0.1") ?: "192.168.0.1"

    fun getPort(): String = prefs.getString("port", "8080") ?: "8080"

    fun saveIp(ip: String) {
        prefs.edit().putString("ip", ip).apply()
    }

    fun savePort(port: String) {
        prefs.edit().putString("port", port).apply()
    }
}
