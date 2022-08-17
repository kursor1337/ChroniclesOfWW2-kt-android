package com.kursor.chroniclesofww2

import com.kursor.chroniclesofww2.domain.interfaces.ISettingsRepository
import com.kursor.chroniclesofww2.domain.interfaces.ITokenHandler
import com.kursor.chroniclesofww2.features.ChangePasswordReceiveDTO
import com.kursor.chroniclesofww2.objects.Const
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Settings(private val settingsRepository: ISettingsRepository) : ITokenHandler {

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

    override var token: String? = null

    private fun changePassword(password: String, httpClient: HttpClient) {
        if (token == null) return
        settingsRepository.password = password
        CoroutineScope(Dispatchers.IO).launch {
            httpClient.put("${Const.connection.URL}/users/change_password") {
                bearerAuth(token ?: return@launch)
                contentType(ContentType.Application.Json)
                setBody(ChangePasswordReceiveDTO(password))
            }
        }
    }


}