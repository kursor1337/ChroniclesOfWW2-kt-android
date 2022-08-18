package com.kursor.chroniclesofww2.data.repositories.user

import com.kursor.chroniclesofww2.domain.interfaces.UserRepository
import com.kursor.chroniclesofww2.features.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class UserRepositoryImpl(val protocol: String, val serverUrl: String, val httpClient: HttpClient) :
    UserRepository {
    override suspend fun login(loginReceiveDTO: LoginReceiveDTO): LoginResponseDTO {
        val response = httpClient.post(Routes.Users.LOGIN.absolutePath(protocol, serverUrl)) {
            contentType(ContentType.Application.Json)
            setBody(loginReceiveDTO)
        }
        return response.body()
    }

    override suspend fun register(registerReceiveDTO: RegisterReceiveDTO): RegisterResponseDTO {
        val response = httpClient.post(Routes.Users.REGISTER.absolutePath(protocol, serverUrl)) {
            contentType(ContentType.Application.Json)
            setBody(registerReceiveDTO)
        }
        return response.body()
    }

    override suspend fun changePassword(
        token: String,
        changePasswordReceiveDTO: ChangePasswordReceiveDTO
    ): ChangePasswordResponseDTO {
        val response =
            httpClient.put(Routes.Users.CHANGE_PASSWORD.absolutePath(protocol, serverUrl)) {
                contentType(ContentType.Application.Json)
                bearerAuth(token)
                setBody(changePasswordReceiveDTO)
            }
        return response.body()
    }

    override suspend fun updateUserInfo(
        token: String,
        updateUserInfoReceiveDTO: UpdateUserInfoReceiveDTO
    ): UpdateUserInfoResponseDTO {
        val response =
            httpClient.put(Routes.Users.UPDATE_USER_INFO.absolutePath(protocol, serverUrl)) {
                contentType(ContentType.Application.Json)
                bearerAuth(token)
                setBody(updateUserInfoReceiveDTO)
            }
        return response.body()

    }

    override suspend fun getUserInfoList(): List<UserInfo> {
        val response = httpClient.get(Routes.Users.GET_ALL.absolutePath(protocol, serverUrl))
        return response.body()
    }

    override suspend fun getUserInfoByLogin(login: String): UserInfo? {
        val response =
            httpClient.get(Routes.Users.GET_BY_LOGIN(login).absolutePath(protocol, serverUrl))
        return response.body()
    }
}