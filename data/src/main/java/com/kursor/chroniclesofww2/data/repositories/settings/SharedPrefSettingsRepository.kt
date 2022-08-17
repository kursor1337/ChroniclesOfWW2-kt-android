package com.kursor.chroniclesofww2.data.repositories.settings

import android.content.Context
import com.kursor.chroniclesofww2.domain.interfaces.SettingsRepository

class SharedPrefSettingsRepository(val context: Context) : SettingsRepository {

    val sharedPreferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)

    override var username: String = sharedPreferences.getString(USERNAME, "") ?: ""
        set(value) {
            field = value
            sharedPreferences.edit().putString(USERNAME, value).apply()
        }
    override var login: String? = sharedPreferences.getString(LOGIN, null)
        set(value) {
            field = value
            sharedPreferences.edit().putString(LOGIN, value).apply()
        }
    override var password: String? = sharedPreferences.getString(PASSWORD, null)
        set(value) {
            field = value
            sharedPreferences.edit().putString(PASSWORD, value).apply()
        }


    companion object {
        const val SETTINGS = "settings"
        const val USERNAME = "username"
        const val LOGIN = "Login"
        const val PASSWORD = "Password"
    }
}