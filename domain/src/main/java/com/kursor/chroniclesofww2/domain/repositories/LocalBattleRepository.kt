package com.kursor.chroniclesofww2.domain.repositories

import com.kursor.chroniclesofww2.model.serializable.Battle

interface LocalBattleRepository : BattleRepository {

    fun nextBattleId(): Int

    fun saveBattle(battle: Battle)

}