package com.kursor.chroniclesofww2.model

import android.util.Log
import com.kursor.chroniclesofww2.model.board.AddMove
import com.kursor.chroniclesofww2.model.board.Board
import com.kursor.chroniclesofww2.model.board.MotionMove
import com.kursor.chroniclesofww2.model.board.Move

class Engine(
    boardSize: Int = Board.DEFAULT_SIZE,
    scenario: Scenario,
    val listener: Listener
) {


    private val board = Board(boardSize)
    private val me = scenario.me
    private val enemy = scenario.enemy
    private var turn = 0

    init {
        if (me.turnMod == 1) listener.onStartingSecond()
    }

//    fun myOwnership(division: Division): Boolean {
//        return myTurn() && division.getKeeper() === me
//    }

    private fun nextTurn() {
        Log.i(TAG, "Next turn")
        turn++
        if (meLost()) listener.onGameEnd(meWon = false)
        else if (enemyLost()) listener.onGameEnd(meWon = true)
    }

    private fun meLost(): Boolean {
        if (board.safeLine(enemy.name, board.height - 1)) return true
        if (me.divisionResources.divisionCount + board.getListOfDivisions(me).size == 0) return true
        return false
    }

    private fun enemyLost(): Boolean {
        if (board.safeLine(me.name, 0)) return true
        if (enemy.divisionResources.divisionCount + board.getListOfDivisions(enemy).size == 0) return true
        return false
    }


    fun handleEnemyMove(move: Move) {
        when (move.type) {
            Move.Type.ADD -> handleAddMove(move as AddMove)
            Move.Type.MOTION -> handleMotionMove(move as MotionMove)
        }
        listener.onEnemyMoveComplete(move)
    }

    fun handleMyMove(move: Move) {
        when (move.type) {
            Move.Type.ADD -> handleAddMove(move as AddMove)
            Move.Type.MOTION -> handleMotionMove(move as MotionMove)
        }
        listener.onMyMoveComplete(move)
    }

    private fun handleAddMove(move: AddMove) {
        Log.i(TAG, "Handle my add move")
        if (!move.tile.isEmpty) return
        move.tile.division = move.divisionReserve.getNewDivision()
        nextTurn()
    }

    private fun handleMotionMove(move: MotionMove) {
        Log.i(TAG, "Handle my motion move")
        if (move.start.isEmpty) return
        if (!move.start.division!!.isValidMove(move)) return
        move.start.division!!.moveOrAttack(move)
        nextTurn()
    }

    companion object {
        const val TAG = "Engine"
    }


    interface Listener {
        fun onMyMoveComplete(move: Move)
        fun onEnemyMoveComplete(move: Move)
        fun onGameEnd(meWon: Boolean)
        fun onStartingSecond()

    }
}