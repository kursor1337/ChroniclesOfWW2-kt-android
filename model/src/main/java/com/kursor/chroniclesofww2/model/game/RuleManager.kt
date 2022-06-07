package com.kursor.chroniclesofww2.model.game

import com.kursor.chroniclesofww2.model.data.GameData
import com.kursor.chroniclesofww2.model.game.moves.AddMove
import com.kursor.chroniclesofww2.model.game.moves.MotionMove

/**
 * this class is used to manage game rules
 */

internal class RuleManager(
    val model: Model
) {
    private var turn = 0

    private val myRow: Int
    private val enemyRow: Int

    init {
        if (model.me.isInitiator) {
            myRow = 0
            enemyRow = model.board.height - 1
        } else {
            myRow = model.board.height - 1
            enemyRow = 0
        }
    }


    private fun meLost(): Boolean {
        if (model.board.isLineSafe(myRow, model.enemy.name)) return true
        if (model.me.divisionResources.divisionCount +
            model.board.getListOfDivisions(model.me).size == 0
        ) return true
        return false
    }

    private fun enemyLost(): Boolean {
        if (model.board.isLineSafe(enemyRow, model.me.name)) return true
        if (model.enemy.divisionResources.divisionCount +
            model.board.getListOfDivisions(model.enemy).size == 0
        ) return true
        return false
    }

    private fun nextTurn() {
        turn++
    }

    fun myTurn() = model.me.isInitiator == (turn % 2 == 0)

    fun enemyTurn() = !myTurn()

    fun isValidMotionMove(motionMove: MotionMove): Boolean {
        if (motionMove.start.division == null) return false
        return motionMove.start.division!!.isValidMove(motionMove)
    }

    fun isValidAddMove(addMove: AddMove): Boolean {
        return addMove.tile.row == myRow
    }

}