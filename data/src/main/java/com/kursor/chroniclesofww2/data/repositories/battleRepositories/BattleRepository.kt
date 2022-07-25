package com.kursor.chroniclesofww2.data.repositories.battleRepositories

import com.kursor.chroniclesofww2.model.data.Battle
import com.kursor.chroniclesofww2.model.game.Nation
import com.kursor.chroniclesofww2.model.game.board.Division

interface BattleRepository {

    val battleList: List<Battle>

    fun findBattleById(id: Int): Battle

    fun defaultBattle(): Battle {
        return Battle(
            -1, "Default", "", Battle.Data(
                -1, Nation.DEFAULT, mapOf(
                    Division.Type.INFANTRY to 9,
                    Division.Type.ARMORED to 3,
                    Division.Type.ARTILLERY to 3
                ),
                Nation.DEFAULT, mapOf(
                    Division.Type.INFANTRY to 9,
                    Division.Type.ARMORED to 3,
                    Division.Type.ARTILLERY to 3
                )
            )
        )
    }

}