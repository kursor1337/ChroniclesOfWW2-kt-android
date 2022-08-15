package com.kursor.chroniclesofww2

import com.kursor.chroniclesofww2.data.repositories.settingsRepositories.SettingsRepository
import com.kursor.chroniclesofww2.data.repositories.settingsRepositories.SharedPrefSettingsRepository
import com.kursor.chroniclesofww2.objects.Const
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject

class Settings(private val settingsRepository: SettingsRepository) {

    var username: String
        get() = settingsRepository.username
        set(value) {
            settingsRepository.username = value
        }

    var login: String?
        get() = settingsRepository.login
        set(value) {
            settingsRepository.login = value
        }

    val password: String?
        get() = settingsRepository.password

    var token: String = ""

    private fun changePassword(password: String, httpClient: HttpClient) {
        settingsRepository.password = password
        CoroutineScope(Dispatchers.IO).launch {
            httpClient.put("${Const.connection.URL}/users/change_password") {
                bearerAuth(token)
                TODO()
            }
        }

    }


}