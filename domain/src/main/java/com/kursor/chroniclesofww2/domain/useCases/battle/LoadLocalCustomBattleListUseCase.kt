package com.kursor.chroniclesofww2.domain.useCases.battle

import com.kursor.chroniclesofww2.domain.interfaces.LocalBattleRepository
import com.kursor.chroniclesofww2.model.serializable.Battle

class LoadLocalCustomBattleListUseCase(
    val battleRepository: LocalBattleRepository
) {


    operator fun invoke(): List<Battle> {
        return battleRepository.battleList
    }


}