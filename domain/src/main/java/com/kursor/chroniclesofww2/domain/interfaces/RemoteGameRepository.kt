package com.kursor.chroniclesofww2.domain.interfaces

import com.kursor.chroniclesofww2.features.CreateGameReceiveDTO
import com.kursor.chroniclesofww2.features.CreateGameResponseDTO
import com.kursor.chroniclesofww2.features.JoinGameReceiveDTO
import com.kursor.chroniclesofww2.features.JoinGameResponseDTO

interface RemoteGameRepository {

    suspend fun createGame(createGameReceiveDTO: CreateGameReceiveDTO): CreateGameResponseDTO

    suspend fun joinGame(joinGameReceiveDTO: JoinGameReceiveDTO): JoinGameResponseDTO

}