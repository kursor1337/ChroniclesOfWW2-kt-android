package com.kursor.chroniclesofww2

import com.kursor.chroniclesofww2.data.repositories.settingsRepositories.SettingsRepository
import com.kursor.chroniclesofww2.features.ChangePasswordReceiveDTO
import com.kursor.chroniclesofww2.objects.Const
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                contentType(ContentType.Application.Json)
                setBody(ChangePasswordReceiveDTO(password))
            }
        }
    }


}