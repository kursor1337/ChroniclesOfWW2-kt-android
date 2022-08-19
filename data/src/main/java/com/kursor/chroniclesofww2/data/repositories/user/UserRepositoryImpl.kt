package com.kursor.chroniclesofww2.data.repositories.user

import com.kursor.chroniclesofww2.domain.repositories.UserRepository
import com.kursor.chroniclesofww2.features.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class UserRepositoryImpl(val serverUrl: String, val httpClient: HttpClient) :
    UserRepository {
    override suspend fun login(loginReceiveDTO: LoginReceiveDTO): LoginResponseDTO {
        val response = httpClient.post(Routes.Users.LOGIN.absolutePath(serverUrl)) {
            contentType(ContentType.Application.Json)
            setBody(loginReceiveDTO)
        }
        return response.body()
    }

    override suspend fun register(registerReceiveDTO: RegisterReceiveDTO): RegisterResponseDTO {
        val response = httpClient.post(Routes.Users.REGISTER.absolutePath(serverUrl)) {
            contentType(ContentType.Application.Json)
            setBody(registerReceiveDTO)
        }
        return response.body()
    }

    override suspend fun getUserInfoList(): List<UserInfo> {
        val response = httpClient.get(Routes.Users.GET_ALL.absolutePath(serverUrl))
        return response.body()
    }

    override suspend fun getUserInfoByLogin(login: String): UserInfo? {
        val response =
            httpClient.get(Routes.Users.GET_BY_LOGIN(login).absolutePath(serverUrl))
        return response.body()
    }
}