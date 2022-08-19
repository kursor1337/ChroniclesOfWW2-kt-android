package com.kursor.chroniclesofww2.domain.repositories

import com.kursor.chroniclesofww2.features.*

interface RemoteGameRepository {

    suspend fun createGame(createGameReceiveDTO: CreateGameReceiveDTO): CreateGameResponseDTO

    suspend fun joinGame(joinGameReceiveDTO: JoinGameReceiveDTO): JoinGameResponseDTO

    suspend fun getWaitingGamesList(): List<WaitingGameInfoDTO>

}