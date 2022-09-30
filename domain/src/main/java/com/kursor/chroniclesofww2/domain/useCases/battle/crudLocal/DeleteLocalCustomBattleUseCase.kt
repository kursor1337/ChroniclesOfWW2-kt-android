package com.kursor.chroniclesofww2.domain.useCases.battle.crudLocal

import com.kursor.chroniclesofww2.domain.repositories.LocalCustomBattleRepository
import com.kursor.chroniclesofww2.domain.tools.RequestResult
import com.kursor.chroniclesofww2.domain.tools.tryRequest
import com.kursor.chroniclesofww2.model.serializable.Battle

class DeleteLocalCustomBattleUseCase(
    val localCustomBattleRepository: LocalCustomBattleRepository
) {

    suspend operator fun invoke(battle: Battle): RequestResult<Unit> = tryRequest {
        localCustomBattleRepository.deleteBattle(battle)
    }

    suspend operator fun invoke(id: Int): RequestResult<Unit> = tryRequest {
        localCustomBattleRepository.deleteBattle(id)
    }

}