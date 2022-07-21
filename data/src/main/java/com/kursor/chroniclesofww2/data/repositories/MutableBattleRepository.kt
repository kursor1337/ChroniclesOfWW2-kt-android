package com.kursor.chroniclesofww2.data.repositories

import com.kursor.chroniclesofww2.model.data.Battle

interface MutableBattleRepository : BattleRepository {

    fun nextBattleId(): Int

    fun saveBattle(battle: Battle)

}