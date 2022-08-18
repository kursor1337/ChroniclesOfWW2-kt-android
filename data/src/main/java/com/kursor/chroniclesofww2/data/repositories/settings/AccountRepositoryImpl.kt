package com.kursor.chroniclesofww2.data.repositories.settings

import android.content.Context
import com.kursor.chroniclesofww2.domain.interfaces.AccountRepository
import com.kursor.chroniclesofww2.features.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountRepositoryImpl(
    val context: Context,
    val httpClient: HttpClient,
    val serverUrl: String
) : AccountRepository {

    val sharedPreferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)

    override var username: String = sharedPreferences.getString(USERNAME, "") ?: ""
        set(value) {
            field = value
            sharedPreferences.edit().putString(USERNAME, value).apply()
        }

    override var token: String? = null

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


    override suspend fun changePassword(
        changePasswordReceiveDTO: ChangePasswordReceiveDTO
    ): ChangePasswordResponseDTO {
        password = changePasswordReceiveDTO.newPassword
        val response = httpClient.put(Routes.Account.CHANGE_PASSWORD.absolutePath(serverUrl)) {
            contentType(ContentType.Application.Json)
            bearerAuth(token ?: "")
            setBody(changePasswordReceiveDTO)
        }
        return response.body()
    }

    override suspend fun updateUserInfo(
        updateUserInfoReceiveDTO: UpdateUserInfoReceiveDTO
    ): UpdateUserInfoResponseDTO {
        val response =
            httpClient.put(Routes.Account.UPDATE_USER_INFO.absolutePath(serverUrl)) {
                contentType(ContentType.Application.Json)
                bearerAuth(token ?: "")
                setBody(updateUserInfoReceiveDTO)
            }
        return response.body()
    }

    override fun auth() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = httpClient.post(Routes.Users.LOGIN.absolutePath(serverUrl)) {
                contentType(ContentType.Application.Json)
                setBody(
                    LoginReceiveDTO(
                        login = login ?: return@post,
                        password = password ?: return@post
                    )
                )
            }
            val loginResponseDTO = response.body<LoginResponseDTO>()
            if (loginResponseDTO.token != null) token = loginResponseDTO.token
        }
    }

    companion object {
        const val SETTINGS = "settings"
        const val USERNAME = "username"
        const val LOGIN = "Login"
        const val PASSWORD = "Password"
    }
}