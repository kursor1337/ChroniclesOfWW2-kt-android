package com.kursor.chroniclesofww2.core

import com.kursor.chroniclesofww2.core.board.Division

data class Scenario(
    val id: Int,
    val localizedName: String,
    val localizedDescription: String,
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
    ) {

    }
}