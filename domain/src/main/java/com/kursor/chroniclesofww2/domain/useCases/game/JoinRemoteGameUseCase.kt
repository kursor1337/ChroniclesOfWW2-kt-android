package com.kursor.chroniclesofww2.domain.useCases.game

import com.kursor.chroniclesofww2.domain.repositories.RemoteGameRepository
import com.kursor.chroniclesofww2.features.JoinGameReceiveDTO
import com.kursor.chroniclesofww2.features.JoinGameResponseDTO
import java.net.http.HttpClient

class JoinRemoteGameUseCase(
    val gameRepository: RemoteGameRepository
) {

    suspend operator fun invoke(joinGameReceiveDTO: JoinGameReceiveDTO): JoinGameResponseDTO {
        return gameRepository.joinGame(joinGameReceiveDTO)
    }


}