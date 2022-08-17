package com.kursor.chroniclesofww2.data.repositories.battle

import com.kursor.chroniclesofww2.model.serializable.Battle

interface MutableBattleRepository : BattleRepository {

    fun nextBattleId(): Int

    fun saveBattle(battle: Battle)

}