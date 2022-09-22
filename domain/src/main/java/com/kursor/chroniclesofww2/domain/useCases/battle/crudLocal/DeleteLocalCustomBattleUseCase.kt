package com.kursor.chroniclesofww2.domain.useCases.battle.crudLocal

import com.kursor.chroniclesofww2.domain.repositories.LocalCustomBattleRepository
import com.kursor.chroniclesofww2.model.serializable.Battle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteLocalCustomBattleUseCase(
    val localCustomBattleRepository: LocalCustomBattleRepository
) {

    suspend operator fun invoke(battle: Battle) = kotlin.runCatching {
        withContext(Dispatchers.IO) {
            localCustomBattleRepository.deleteBattle(battle)
        }
    }

    suspend operator fun invoke(id: Int) = kotlin.runCatching {
        withContext(Dispatchers.IO) {
            localCustomBattleRepository.deleteBattle(id)
        }
    }

}