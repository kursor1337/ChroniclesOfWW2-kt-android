package com.kursor.chroniclesofww2.data.repositories.game

import com.kursor.chroniclesofww2.domain.repositories.RemoteGameRepository
import com.kursor.chroniclesofww2.features.Routes
import com.kursor.chroniclesofww2.features.WaitingGameInfoDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class RemoteGameRepositoryImpl(
    val httpClient: HttpClient,
    val serverUrl: String
) : RemoteGameRepository {

    override suspend fun getWaitingGamesList(token: String): List<WaitingGameInfoDTO> {
        val response = httpClient.get(Routes.Game.absolutePath(serverUrl)) {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
        }
        return response.body()
    }
}