package com.kursor.chroniclesofww2.domain.useCases.battle

import com.kursor.chroniclesofww2.domain.repositories.LocalCustomBattleRepository
import com.kursor.chroniclesofww2.model.serializable.Battle

class LoadLocalCustomBattleListUseCase(
    val battleRepository: LocalCustomBattleRepository
) {


    operator fun invoke(): List<Battle> {
        return battleRepository.battleList
    }


}