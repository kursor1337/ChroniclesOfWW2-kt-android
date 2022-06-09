package com.kursor.chroniclesofww2.data.repositories

import com.kursor.chroniclesofww2.model.data.Battle

interface BattleRepository {

    val battleList: List<Battle>

    fun findBattleById(id: Int): Battle

}