package com.kursor.chroniclesofww2.data.repositories

import com.kursor.chroniclesofww2.model.data.Battle

class RemoteCustomBattleRepository : BattleRepository {
    override val battleList: List<Battle>
        get() = TODO("Not yet implemented")

    override fun findBattleById(id: Int): Battle {
        TODO("Not yet implemented")
    }
}