package com.kursor.chroniclesofww2.domain.useCases.battle.load

import com.kursor.chroniclesofww2.domain.repositories.LocalCustomBattleRepository
import com.kursor.chroniclesofww2.model.serializable.Battle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadLocalCustomBattleListUseCase(
    val battleRepository: LocalCustomBattleRepository
) {


    suspend operator fun invoke() = kotlin.runCatching {
        withContext(Dispatchers.IO) {
            battleRepository.getAllBattles()
        }
    }


}