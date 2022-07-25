package com.kursor.chroniclesofww2.data.repositories.settingsRepositories

import android.content.Context

class SharedPrefSettingsRepository(val context: Context) : SettingsRepository {

    val sharedPreferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)

    override var username: String = sharedPreferences.getString(USERNAME, "") ?: ""
        set(value) {
            field = value
            sharedPreferences.edit().putString(USERNAME, field).apply()
        }

    companion object {
        const val SETTINGS = "settings"
        const val USERNAME = "username"
    }
}