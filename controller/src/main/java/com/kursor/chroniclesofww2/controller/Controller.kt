package com.kursor.chroniclesofww2.controller

import com.kursor.chroniclesofww2.model.game.Model
import com.kursor.chroniclesofww2.model.game.Reserve
import com.kursor.chroniclesofww2.model.game.RuleManager
import com.kursor.chroniclesofww2.model.game.board.Division
import com.kursor.chroniclesofww2.model.game.board.Tile
import com.kursor.chroniclesofww2.model.game.moves.AddMove
import com.kursor.chroniclesofww2.model.game.moves.MotionMove
import com.kursor.chroniclesofww2.model.game.moves.Move

abstract class Controller(
    protected val model: Model,
    protected val listener: Listener
) {

    protected val ruleManager = RuleManager(model)

    protected var clickedTile: Tile? = null
        set(value) {
            if (field == null && value != null) listener.onTileClicked(
                value,
                ruleManager.calculatePossibleMotionMoves(
                    value.row,
                    value.column,
                    value.division!!.playerName
                )
            )
            field = value
        }
    protected var clickedReserve: Reserve? = null
        set(value) {
            if (field == null && value != null) listener.onReserveClicked(
                value,
                ruleManager.calculatePossibleAddMoves(value.type, value.playerName)
            )
            field = value
        }

    abstract fun processTileClick(i: Int, j: Int)

    abstract fun processReserveClick(type: Division.Type, playerName: String)

    fun checkAndSendMoveToModel(move: Move) {
        if (!ruleManager.checkMoveForValidity(move)) return
        if (move.type == Move.Type.MOTION) {
            move as MotionMove
            model.handleMotionMove(move)
            listener.onMotionMoveComplete(move)
        } else {
            move as AddMove
            model.handleAddMove(move)
            listener.onAddMoveComplete(move)
        }
        ruleManager.nextTurn()
        if (ruleManager.meLost()) listener.onGameEnd(meWon = false)
        if (ruleManager.enemyLost()) listener.onGameEnd(meWon = true)
    }

    interface Listener {
        //on completed moves
        fun onMotionMoveComplete(motionMove: MotionMove)
        fun onAddMoveComplete(addMove: AddMove)
        fun onEnemyMoveComplete(move: Move)

        //on moves canceled
        fun onMotionMoveCanceled(i: Int, j: Int)
        fun onAddMoveCanceled()

        // on clicked
        fun onReserveClicked(reserve: Reserve, possibleMoves: List<Move>)
        fun onTileClicked(tile: Tile, possibleMoves: List<Move>)

        // on game events
        fun onGameEnd(meWon: Boolean)
        fun onStartingSecond()
    }

}