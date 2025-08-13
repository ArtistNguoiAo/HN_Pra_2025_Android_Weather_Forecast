package com.sun.weatherapp.data.helper

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {

    companion object {
        private const val PREF_NAME = "my_shared_pref"
        const val KEY_REMEMBER_ME = "remember_me"
        const val KEY_EMAIL = "email"
        const val KEY_PASSWORD = "password"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    // Lưu dữ liệu
    fun putString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    // Lấy dữ liệu
    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    // Xóa dữ liệu
    fun remove(key: String) {
        editor.remove(key).apply()
    }

    // Xóa tất cả
    fun clear() {
        editor.clear().apply()
    }
}
