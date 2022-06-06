package com.kursor.chroniclesofww2.core

import com.kursor.chroniclesofww2.core.board.*
import com.kursor.chroniclesofww2.core.moves.Move


class Engine(
    gameData: GameData,
    val listener: Listener
) {

    private val board = Board(gameData.boardHeight, gameData.boardWidth)
    private val me = gameData.me
    private val enemy = gameData.enemy
    private var turn = 0

    private var clickedTile: Tile? = null
        set(value) {
            if (field == null && value != null) listener.onTileClicked(value)
        }
    private var clickedReserve: Reserve? = null

    init {
        if (me.turnMod == 1) listener.onStartingSecond()
    }

//    fun myOwnership(division: Division): Boolean {
//        return myTurn() && division.getKeeper() === me
//    }


    fun processTileClick(i: Int, j: Int) {
        val tile = board[i, j]
        if (tile == clickedTile) {
            clickedTile = null
            listener.onMyMotionMoveCanceled(i, j)
            return
        }
        when {
            clickedTile != null -> {
                val valid = handleMyMove(MotionMove(clickedTile!!, tile))
                if (valid) clickedTile = null
                return

            }
            clickedReserve != null -> {
                val valid = handleMyMove(AddMove(clickedReserve!!, tile))
                if (valid) clickedReserve = null
                return

            }
            else -> { // if both clickedTile and clickedReserve == null
                // we need to set clickedTile
                if (tile.isEmpty || tile.division!!.playerName == enemy.name) return
                clickedTile = tile
            }
        }
//        if (clickedTile == null && (tile.isEmpty || tile.division!!.playerName == enemy.name)) return
//        if (tile.isEmpty && clickedReserve != null) {
//
//        }
//        if (clickedTile != null && (tile.isEmpty || tile.division!!.playerName ==))
    }

    fun processReserveClick(type: Division.Type) {
        val reserve = me.divisionResources.reserves[type]!!
        if (reserve == clickedReserve) {
            clickedReserve = null
            listener.onMyAddMoveCanceled()
        }
    }

    private fun nextTurn() {
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
            Move.Type.ADD -> handleAddMove(move as AddMove, board.height - 1)
            Move.Type.MOTION -> handleMotionMove(move as MotionMove)
        }
        listener.onEnemyMoveComplete(move)
    }

    fun handleMyMove(move: Move): Boolean {
        val valid = when (move.type) {
            Move.Type.ADD -> handleAddMove(move as AddMove, 0)
            Move.Type.MOTION -> handleMotionMove(move as MotionMove)
        }
        if (valid) listener.onMyMoveComplete(move)
        return valid
    }

    private fun handleAddMove(move: AddMove, startingRow: Int): Boolean {
        if (!move.tile.isEmpty || move.tile.row != startingRow) return false
        move.tile.division = move.divisionReserve.getNewDivision()
        nextTurn()
        return true
    }

    private fun handleMotionMove(move: MotionMove): Boolean {
        if (move.start.isEmpty) return false
        if (!move.destination.isEmpty &&
            move.start.division!!.playerName == move.destination.division!!.playerName
        ) return false
        if (!move.start.division!!.isValidMove(move)) return false
        move.start.division!!.moveOrAttack(move)
        nextTurn()
        return true
    }

    companion object {
        const val TAG = "Engine"
    }


    interface Listener {
        fun onMyMoveComplete(move: Move)
        fun onEnemyMoveComplete(move: Move)
        fun onGameEnd(meWon: Boolean)
        fun onStartingSecond()
        fun onMyMotionMoveCanceled(i: Int, j: Int)
        fun onMyAddMoveCanceled()
        fun onTileClicked(tile: Tile)

    }
}