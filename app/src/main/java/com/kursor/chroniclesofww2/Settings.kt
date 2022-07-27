package com.kursor.chroniclesofww2

import com.kursor.chroniclesofww2.data.repositories.settingsRepositories.SettingsRepository
import com.kursor.chroniclesofww2.data.repositories.settingsRepositories.SharedPrefSettingsRepository
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject

class Settings(private val settingsRepository: SettingsRepository) {

    var username: String
        get() = settingsRepository.username
        set(value) {
            settingsRepository.username = value
        }

}