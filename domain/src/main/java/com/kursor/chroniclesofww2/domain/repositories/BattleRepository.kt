package com.kursor.chroniclesofww2.domain.repositories

import com.kursor.chroniclesofww2.model.game.Nation
import com.kursor.chroniclesofww2.model.game.board.Division
import com.kursor.chroniclesofww2.model.serializable.Battle

interface BattleRepository {

    val PREFIX: Int

    val battleList: List<Battle>

    fun findBattleById(id: Int): Battle?

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