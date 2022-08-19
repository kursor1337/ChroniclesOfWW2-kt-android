package com.kursor.chroniclesofww2.data.repositories.game

import com.kursor.chroniclesofww2.domain.interfaces.AccountRepository
import com.kursor.chroniclesofww2.domain.interfaces.RemoteGameRepository
import com.kursor.chroniclesofww2.features.CreateGameReceiveDTO
import com.kursor.chroniclesofww2.features.CreateGameResponseDTO
import com.kursor.chroniclesofww2.features.JoinGameReceiveDTO
import com.kursor.chroniclesofww2.features.JoinGameResponseDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class RemoteGameRepositoryImpl(
    val httpClient: HttpClient,
    val serverUrl: String,
    val accountRepository: AccountRepository
) : RemoteGameRepository {


    override suspend fun createGame(createGameReceiveDTO: CreateGameReceiveDTO): CreateGameResponseDTO {
        if (accountRepository.token == null) accountRepository.auth()

        val response = httpClient.post(serverUrl) {
            contentType(ContentType.Application.Json)
            bearerAuth(accountRepository.token ?: "")
            setBody(createGameReceiveDTO)
        }
        if (response.status == HttpStatusCode.Unauthorized) {
            accountRepository.auth()
            return createGame(createGameReceiveDTO)
        }
        return response.body()
    }


    override suspend fun joinGame(joinGameReceiveDTO: JoinGameReceiveDTO): JoinGameResponseDTO {
        val response = httpClient.post(serverUrl) {
            contentType(ContentType.Application.Json)
            bearerAuth(accountRepository.token ?: "")
            setBody(joinGameReceiveDTO)
        }
        if (response.status == HttpStatusCode.Unauthorized) {
            accountRepository.auth()
            return joinGame(joinGameReceiveDTO)
        }
        return response.body()
    }
}