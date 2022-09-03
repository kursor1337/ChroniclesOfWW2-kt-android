package com.kursor.chroniclesofww2.domain.useCases.battle

import com.kursor.chroniclesofww2.domain.repositories.RemoteCustomBattleRepository
import com.kursor.chroniclesofww2.model.serializable.Battle

class LoadRemoteCustomBattleListUseCase(
    val remoteCustomBattleRepository: RemoteCustomBattleRepository
) {

    suspend operator fun invoke(): Result<List<Battle>> = runCatching {
        remoteCustomBattleRepository.getAllBattles()
    }

}