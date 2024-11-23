package com.example.jobder

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {
    private const val PREFS_NAME = "app_prefs"
    private const val KEY_IS_DARK_MODE = "isDarkMode"
    private const val KEY_LANGUAGE = "language"

    fun savePreferences(context: Context, isDarkMode: Boolean, language: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean(KEY_IS_DARK_MODE, isDarkMode)
            putString(KEY_LANGUAGE, language)
            apply()
        }
    }

    fun loadPreferences(context: Context): Pair<Boolean, String> {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean(KEY_IS_DARK_MODE, false)
        val language = sharedPreferences.getString(KEY_LANGUAGE, "en") ?: "en"
        return Pair(isDarkMode, language)
    }
}