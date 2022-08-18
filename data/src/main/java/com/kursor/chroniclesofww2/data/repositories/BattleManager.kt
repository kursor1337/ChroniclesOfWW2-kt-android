package com.kursor.chroniclesofww2.data.repositories

import com.kursor.chroniclesofww2.domain.interfaces.BattleRepository
import com.kursor.chroniclesofww2.model.serializable.Battle

class BattleManager(val repositories: List<BattleRepository>) {

    fun findBattleById(id: Int): Battle? {
        for (repo in repositories) {
            if ((id - repo.PREFIX) / 1_000_000_000 != 0) continue
            return repo.findBattleById(id)
        }
        return null
    }
}