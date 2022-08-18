package com.kursor.chroniclesofww2.domain.useCases.battle

import com.kursor.chroniclesofww2.domain.interfaces.RemoteCustomBattleRepository
import com.kursor.chroniclesofww2.model.serializable.Battle

class LoadRemoteCustomBattleListUseCase(
    val remoteCustomBattleRepository: RemoteCustomBattleRepository
) {

    suspend operator fun invoke(): List<Battle> {
        return remoteCustomBattleRepository.getAllBattles()
    }

}