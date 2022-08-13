package com.kursor.chroniclesofww2.model.game

import com.kursor.chroniclesofww2.model.data.GameData
import com.kursor.chroniclesofww2.model.data.GamePlayer
import com.kursor.chroniclesofww2.model.game.board.*
import com.kursor.chroniclesofww2.model.game.moves.AddMove
import com.kursor.chroniclesofww2.model.game.moves.MotionMove
import java.lang.IllegalArgumentException


class Model(gameData: GameData) {

    val board = Board(gameData.boardHeight, gameData.boardWidth)
    val me = gameData.me
    val enemy = gameData.enemy

    fun handleAddMove(move: AddMove) {
        move.destination.division = move.divisionReserve.getNewDivision()
    }

    fun handleMotionMove(move: MotionMove) {
        move.start.division!!.moveOrAttack(move)
    }

    fun getPlayerByName(playerName: String): GamePlayer = when (playerName) {
        me.name -> me
        enemy.name -> enemy
        else -> throw IllegalArgumentException("There is no player with such name")
    }
}