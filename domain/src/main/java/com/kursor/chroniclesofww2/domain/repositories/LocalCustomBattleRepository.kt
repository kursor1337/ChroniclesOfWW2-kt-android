package com.kursor.chroniclesofww2.domain.repositories

import com.kursor.chroniclesofww2.model.serializable.Battle

interface LocalCustomBattleRepository : BattleRepository {

    fun nextBattleId(): Int

    suspend fun getAllBattles(): List<Battle>

    suspend fun saveBattle(battle: Battle)

    suspend fun editBattle(battle: Battle)

    suspend fun deleteBattle(battle: Battle)

    suspend fun deleteBattle(id: Int)
}