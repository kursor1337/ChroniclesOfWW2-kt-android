package com.kursor.chroniclesofww2.model.game

import com.kursor.chroniclesofww2.model.data.GameData
import com.kursor.chroniclesofww2.model.game.board.*
import com.kursor.chroniclesofww2.model.game.moves.AddMove
import com.kursor.chroniclesofww2.model.game.moves.MotionMove
import com.kursor.chroniclesofww2.model.game.moves.Move


class Model(gameData: GameData) {

    val board = Board(gameData.boardHeight, gameData.boardWidth)
    val me = gameData.me
    val enemy = gameData.enemy

    fun handleEnemyMove(move: Move) {
        when (move.type) {
            Move.Type.ADD -> handleAddMove(move as AddMove, board.height - 1)
            Move.Type.MOTION -> handleMotionMove(move as MotionMove)
        }
    }

    fun handleMyMove(move: Move) {
        when (move.type) {
            Move.Type.ADD -> handleAddMove(move as AddMove, 0)
            Move.Type.MOTION -> handleMotionMove(move as MotionMove)
        }
    }

    private fun handleAddMove(move: AddMove, startingRow: Int) {
        if (!move.tile.isEmpty || move.tile.row != startingRow) return
        move.tile.division = move.divisionReserve.getNewDivision()
        return
    }

    private fun handleMotionMove(move: MotionMove) {
        if (move.start.isEmpty) return
        if (!move.destination.isEmpty &&
            move.start.division!!.playerName == move.destination.division!!.playerName
        ) return
        if (!move.start.division!!.isValidMove(move)) return
        move.start.division!!.moveOrAttack(move)
        return
    }

//    interface Listener {
//        fun onMyMoveComplete(move: Move)
//        fun onEnemyMoveComplete(move: Move)
//        fun onGameEnd(meWon: Boolean)
//        fun onStartingSecond()
//        fun onMyMotionMoveCanceled(i: Int, j: Int)
//        fun onMyAddMoveCanceled()
//        fun onTileClicked(tile: Tile)
//
//    }
}