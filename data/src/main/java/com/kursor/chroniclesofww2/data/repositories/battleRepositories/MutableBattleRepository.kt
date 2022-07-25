package com.kursor.chroniclesofww2.data.repositories.battleRepositories

import com.kursor.chroniclesofww2.model.data.Battle

interface MutableBattleRepository : BattleRepository {

    fun nextBattleId(): Int

    fun saveBattle(battle: Battle)

}