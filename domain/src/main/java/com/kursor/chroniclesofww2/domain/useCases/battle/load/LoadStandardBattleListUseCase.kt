package com.kursor.chroniclesofww2.domain.useCases.battle.load

import com.kursor.chroniclesofww2.domain.repositories.StandardBattleRepository
import com.kursor.chroniclesofww2.model.serializable.Battle

class LoadStandardBattleListUseCase(
    val standardBattleRepository: StandardBattleRepository
) {

    operator fun invoke(): List<Battle> {
        return standardBattleRepository.battleList
    }

}