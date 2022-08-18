package com.kursor.chroniclesofww2.domain.useCases.battle

import com.kursor.chroniclesofww2.domain.interfaces.StandardBattleRepository
import com.kursor.chroniclesofww2.model.serializable.Battle

class LoadStandardBattleListUseCase(
    val standardBattleRepository: StandardBattleRepository
) {

    operator fun invoke(): List<Battle> {
        return standardBattleRepository.battleList
    }

}