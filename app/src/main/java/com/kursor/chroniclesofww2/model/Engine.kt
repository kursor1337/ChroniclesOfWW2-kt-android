package com.kursor.chroniclesofww2.model

import com.kursor.chroniclesofww2.model.board.Board
import com.kursor.chroniclesofww2.model.board.Move
import com.kursor.chroniclesofww2.model.board.Tile

class Engine(myName: String, enemyName: String) {

    val board = Board()
    val me = Player(myName)
    val enemy = Player(enemyName)

    val enemyMovesListener = object : EnemyListener {
        override fun onEnemyMove(move: Move) {
            TODO()
        }

        override fun onEnemyPlaceDivision(tile: Tile) {
            TODO("Not yet implemented")
        }
    }

    fun processEnemyMove() {

    }

    fun processMyMove() {

    }


    interface Listener {

    }
}