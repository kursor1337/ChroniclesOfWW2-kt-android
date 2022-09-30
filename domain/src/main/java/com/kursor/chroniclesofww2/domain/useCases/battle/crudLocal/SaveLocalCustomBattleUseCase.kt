package com.kursor.chroniclesofww2.domain.useCases.battle.crudLocal

import com.kursor.chroniclesofww2.domain.repositories.LocalCustomBattleRepository
import com.kursor.chroniclesofww2.domain.tools.tryRequest
import com.kursor.chroniclesofww2.model.serializable.Battle

class SaveLocalCustomBattleUseCase(
    val localCustomBattleRepository: LocalCustomBattleRepository
) {

    suspend operator fun invoke(battle: Battle) = tryRequest {
        localCustomBattleRepository.saveBattle(battle)
    }
}