package com.kursor.chroniclesofww2.model.game

import com.kursor.chroniclesofww2.model.game.board.Division
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


    fun meLost(): Boolean {
        if (model.board.isLineSafe(myRow, model.enemy.name)) return true
        if (model.me.divisionResources.divisionCount +
            model.board.getListOfDivisions(model.me).size == 0
        ) return true
        return false
    }

    fun enemyLost(): Boolean {
        if (model.board.isLineSafe(enemyRow, model.me.name)) return true
        if (model.enemy.divisionResources.divisionCount +
            model.board.getListOfDivisions(model.enemy).size == 0
        ) return true
        return false
    }

    fun nextTurn() {
        turn++
    }

    fun myTurn() = model.me.isInitiator == (turn % 2 == 0)

    fun enemyTurn() = !myTurn()

    fun isValidMotionMove(motionMove: MotionMove): Boolean {
        if (motionMove.start.division == null) return false
        return motionMove.start.division!!.isValidMove(motionMove)
    }

    fun isValidAddMove(addMove: AddMove): Boolean {
        return addMove.destination.row == myRow
    }

    fun calculateMyPossibleMotionMoves(i: Int, j: Int): List<MotionMove> =
        calculatePossibleMotionMoves(i, j, model.me.name)

    fun calculateMyPossibleAddMoves(type: Division.Type): List<AddMove> =
        calculatePossibleAddMoves(type, model.me.name)


    private fun calculatePossibleMotionMoves(i: Int, j: Int, playerName: String): List<MotionMove> {
        if (model.board[i, j].isEmpty) return emptyList()
        val division = model.board[i, j].division!!
        val result = mutableListOf<MotionMove>()
        model.board.forEachTile { tile ->
            val move = MotionMove(model.board[i, j], tile)
            if (!division.isValidMove(move)) return@forEachTile
            if (tile.isEmpty || tile.division!!.playerName != playerName)
                result.add(move)
        }
        return result
    }

    private fun calculatePossibleAddMoves(type: Division.Type, playerName: String): List<AddMove> {
        val row = if (model.me.name == playerName) myRow else enemyRow
        return List(model.board.width) { j ->
            AddMove(
                model.me.divisionResources.reserves[type]!!,
                model.board[row, j]
            )
        }.filter { addMove -> addMove.destination.division?.playerName != playerName }
    }

}