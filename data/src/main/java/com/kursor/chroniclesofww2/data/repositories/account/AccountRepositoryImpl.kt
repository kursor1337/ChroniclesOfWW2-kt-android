package com.kursor.chroniclesofww2.data.repositories.account

import android.content.Context
import android.util.Log
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.features.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*

class AccountRepositoryImpl(
    val context: Context,
    val httpClient: HttpClient,
    val serverUrl: String
) : AccountRepository {

    val coroutineScope = CoroutineScope(Dispatchers.IO)

    val sharedPreferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)

    override var username: String = sharedPreferences.getString(USERNAME, "") ?: ""
        set(value) {
            field = value
            sharedPreferences.edit().putString(USERNAME, value).apply()
        }

    override var token: String? = sharedPreferences.getString(TOKEN, null)
        set(value) {
            field = value
            sharedPreferences.edit().putString(TOKEN, value).apply()
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

    private var tokenExpirationDate: Long = sharedPreferences.getLong(TOKEN_EXPIRATION_DATE, -1L)
        set(value) {
            field = value
            sharedPreferences.edit().putLong(TOKEN_EXPIRATION_DATE, value).apply()
        }

    private fun setTokenExpiration(millis: Long) {
        tokenExpirationDate = System.currentTimeMillis() + millis * 2 / 3
        coroutineScope.launch {
            startTokenExpireTimer(millis * 2 / 3)
        }
    }

    override fun refreshTokenInIntervals() {
        coroutineScope.launch {
            kotlin.runCatching {
                if (tokenExpirationDate < System.currentTimeMillis()) {
                    refreshToken()
                }
            }
        }
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

    override suspend fun deleteAccount(deleteUserReceiveDTO: DeleteUserReceiveDTO): DeleteUserResponseDTO {
        val response = httpClient.delete(Routes.Account.DELETE.absolutePath(serverUrl)) {
            bearerAuth(token ?: "")
            setBody(deleteUserReceiveDTO)
        }
        return response.body()
    }

    override suspend fun refreshToken() {
        Log.i("AccountRepository", "Refreshing token")
        if (token == null) {
            if (password == null || login == null) return
            auth()
            if (token == null) return
        }
        val response = httpClient.post(Routes.Account.AUTH.absolutePath(serverUrl)) {
            bearerAuth(token!!)
        }
        if (response.status != HttpStatusCode.OK) auth()
    }


    override suspend fun auth() {
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
        if (loginResponseDTO.token != null) {
            token = loginResponseDTO.token
            setTokenExpiration(loginResponseDTO.expiresIn)
            Log.d("AccountRepository", "token expires in ${loginResponseDTO.expiresIn}")
        } else {
            password = null
            login = null
        }
    }

    override suspend fun getAccountInfo(): AccountInfo? {
        if (token == null) return null
        return httpClient.get(Routes.Account.GET_ACCOUNT_INFO.absolutePath(serverUrl)) {
            bearerAuth(token!!)
        }.body()
    }

    override suspend fun checkCredentials(): Boolean {
        if (login == null || password == null) return false
        val response =
            httpClient.post(
                Routes.Users.LOGIN.absolutePath(serverUrl)
            ) {
                contentType(ContentType.Application.Json)
                setBody(
                    LoginReceiveDTO(
                        login = login ?: return false,
                        password = password ?: return false
                    )
                )
            }
        val loginResponseDTO = response.body<LoginResponseDTO>()
        if (loginResponseDTO.token != null) {
            token = loginResponseDTO.token
            return true
        }
        return false
    }

    override suspend fun checkToken(): Boolean {
        if (token == null) return false
        val response =
            httpClient.post(
                Routes.Account.AUTH.absolutePath(serverUrl)
            ) {
                bearerAuth(token ?: return false)
            }
        return response.status == HttpStatusCode.OK
    }

    override suspend fun isSignedIn(): Boolean {
        return checkToken() || checkCredentials()
    }

    override suspend fun startTokenExpireTimer(millis: Long) {
        withContext(Dispatchers.IO) {
            delay(millis)
            refreshToken()
        }
    }

    companion object {
        const val SETTINGS = "settings"
        const val USERNAME = "username"
        const val LOGIN = "Login"
        const val PASSWORD = "Password"
        const val TOKEN = "Token"
        const val TOKEN_EXPIRATION_DATE = "token expiration date"
    }
}