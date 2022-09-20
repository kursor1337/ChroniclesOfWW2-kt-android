package com.kursor.chroniclesofww2.domain.repositories

import com.kursor.chroniclesofww2.model.serializable.Battle

interface LocalCustomBattleRepository : BattleRepository {

    fun nextBattleId(): Int

    fun saveBattle(battle: Battle)

    fun editBattle(battle: Battle)

    fun deleteBattle(battle: Battle)

    fun deleteBattle(id: Int)
}