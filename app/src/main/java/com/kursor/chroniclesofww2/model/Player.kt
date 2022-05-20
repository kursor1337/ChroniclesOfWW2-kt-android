package com.kursor.chroniclesofww2.model

import com.kursor.chroniclesofww2.model.board.Division
import com.kursor.chroniclesofww2.model.board.Move
import com.kursor.chroniclesofww2.model.board.Tile

class Player(
    val name: String,
    val divisionResources: DivisionResources,
    nation: Nation
) {

    constructor(name: String) : this(
        name,
        DivisionResources.getDefaultInstance(name),
        Nation.DEFAULT
    )

    companion object {
        const val ME = 601
        const val ENEMY = 602
    }


}

interface EnemyListener {
    fun onEnemyMove(move: Move)
    fun onEnemyPlaceDivision(tile: Tile)
}