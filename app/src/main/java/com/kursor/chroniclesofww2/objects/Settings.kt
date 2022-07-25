package com.kursor.chroniclesofww2.objects

import com.kursor.chroniclesofww2.data.repositories.settingsRepositories.SettingsRepository
import com.kursor.chroniclesofww2.data.repositories.settingsRepositories.SharedPrefSettingsRepository
import org.koin.java.KoinJavaComponent.inject

object Settings {

    val username by lazy {
        val repo by inject<SharedPrefSettingsRepository>(SharedPrefSettingsRepository::class.java)
        repo.username
    }

}