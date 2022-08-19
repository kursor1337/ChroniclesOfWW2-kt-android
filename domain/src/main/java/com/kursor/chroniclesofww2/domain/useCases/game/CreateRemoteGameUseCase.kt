package com.kursor.chroniclesofww2.domain.useCases.game

import com.kursor.chroniclesofww2.domain.repositories.RemoteGameRepository
import com.kursor.chroniclesofww2.features.CreateGameReceiveDTO
import com.kursor.chroniclesofww2.features.CreateGameResponseDTO

class CreateRemoteGameUseCase(
    val gameRepository: RemoteGameRepository
) {

    suspend operator fun invoke(createGameReceiveDTO: CreateGameReceiveDTO): CreateGameResponseDTO {
        return gameRepository.createGame(createGameReceiveDTO)
    }

}