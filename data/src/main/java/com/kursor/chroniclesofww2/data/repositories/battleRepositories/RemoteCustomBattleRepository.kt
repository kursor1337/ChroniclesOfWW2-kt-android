package com.kursor.chroniclesofww2.data.repositories.battleRepositories

import com.kursor.chroniclesofww2.model.serializable.Battle

class RemoteCustomBattleRepository : BattleRepository {
    override val PREFIX: Int
        get() = -1_000_000_000
    override val battleList: List<Battle>
        get() = TODO("Not yet implemented")

    override fun findBattleById(id: Int): Battle {
        TODO("Not yet implemented")
    }
}