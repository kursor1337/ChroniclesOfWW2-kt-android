package com.kursor.chroniclesofww2.data.repositories.game

import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.repositories.RemoteGameRepository
import com.kursor.chroniclesofww2.features.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.lang.Exception

class RemoteGameRepositoryImpl(
    val httpClient: HttpClient,
    val serverUrl: String,
    val accountRepository: AccountRepository
) : RemoteGameRepository {

    override suspend fun getWaitingGamesList(): List<WaitingGameInfoDTO> {
        val response = httpClient.get(Routes.Game.absolutePath(serverUrl)) {
            contentType(ContentType.Application.Json)
            bearerAuth(accountRepository.token ?: "")
        }
        if (response.status == HttpStatusCode.Unauthorized) {
            accountRepository.auth()
            if (accountRepository.token != null) return getWaitingGamesList()
        }
        return response.body()
    }
}