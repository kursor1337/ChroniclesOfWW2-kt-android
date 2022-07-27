package com.kursor.chroniclesofww2.model.data

import com.kursor.chroniclesofww2.model.game.Nation
import com.kursor.chroniclesofww2.model.game.board.Division

data class Battle(
    val id: Int,
    val name: String,
    val description: String,
    val data: Data
) {

    companion object {
        const val DEFAULT = -865
        const val DEFAULT_MISSION_NAME = "Default"
    }

    data class Data(
        val id: Int,
        val nation1: Nation,
        val nation1divisions: Map<Division.Type, Int>,
        val nation2: Nation,
        val nation2divisions: Map<Division.Type, Int>
    )

}