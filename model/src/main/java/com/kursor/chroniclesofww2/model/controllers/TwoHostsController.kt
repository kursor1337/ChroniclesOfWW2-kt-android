package com.kursor.chroniclesofww2.model.controllers

import com.kursor.chroniclesofww2.model.game.Model
import com.kursor.chroniclesofww2.model.game.board.Division
import com.kursor.chroniclesofww2.model.game.moves.AddMove
import com.kursor.chroniclesofww2.model.game.moves.MotionMove
import com.kursor.chroniclesofww2.model.game.moves.Move

class TwoHostsController(
    model: Model,
    listener: Listener
) : Controller(model, listener) {

    init {
        if (!model.me.isInitiator) listener.onStartingSecond()
    }

    override fun processTileClick(i: Int, j: Int) {
        if (!ruleManager.isMyTurn()) return
        super.processTileClick(i, j)
    }

    override fun processReserveClick(type: Division.Type, playerName: String) {
        if (!ruleManager.isMyTurn()) return
        super.processReserveClick(type, playerName)
    }


    fun processEnemyMove(move: Move) {
        if (move.type == Move.Type.ADD) model.handleAddMove(move as AddMove)
        else model.handleMotionMove(move as MotionMove)
        ruleManager.nextTurn()
        listener.onEnemyMoveComplete(move)
        if (ruleManager.meLost()) listener.onGameEnd(meWon = false)
        if (ruleManager.enemyLost()) listener.onGameEnd(meWon = true)
    }

    fun processSimplifiedEnemyMove(simplified: Move.Simplified) {
        val move = when (simplified.type) {
            Move.Type.MOTION -> {
                simplified as MotionMove.Simplified
                val startRow = simplified.startCoordinate / 10
                val startColumn = simplified.startCoordinate % 10
                val destRow = simplified.destinationCoordinate / 10
                val destColumn = simplified.destinationCoordinate % 10
                MotionMove(model.board[startRow, startColumn], model.board[destRow, destColumn])
            }
            Move.Type.ADD -> {
                simplified as AddMove.Simplified
                val destRow = simplified.destinationCoordinate / 10
                val destColumn = simplified.destinationCoordinate % 10
                AddMove(
                    model.enemy.divisionResources.reserves[simplified.divisionType]!!,
                    model.board[destRow, destColumn]
                )
            }
        }
        processEnemyMove(move)
    }


}