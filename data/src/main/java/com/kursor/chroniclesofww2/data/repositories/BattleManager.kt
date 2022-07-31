package com.kursor.chroniclesofww2.data.repositories

import com.kursor.chroniclesofww2.data.repositories.battleRepositories.BattleRepository
import com.kursor.chroniclesofww2.model.data.Battle

class BattleManager(val repositories: List<BattleRepository>, ) {

    fun findBattleById(id: Int): Battle? {
        for (repo in repositories) {
            if (id / repo.PREFIX != 1) continue
            return repo.findBattleById(id)
        }
        return null
    }

}