package com.kursor.chroniclesofww2.domain.useCases.game

import com.kursor.chroniclesofww2.domain.repositories.RemoteGameRepository
import com.kursor.chroniclesofww2.features.WaitingGameInfoDTO

class LoadRemoteGameListUseCase(
    val gameRepository: RemoteGameRepository
) {

    suspend operator fun invoke(): List<WaitingGameInfoDTO> {
        return gameRepository.getWaitingGamesList()
    }

}