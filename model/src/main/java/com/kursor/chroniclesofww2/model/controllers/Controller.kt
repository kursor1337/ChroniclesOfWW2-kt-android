package com.kursor.chroniclesofww2.model.controllers

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

    open fun processTileClick(i: Int, j: Int) {
        val tile = model.board[i, j]
        if (tile == clickedTile) {
            clickedTile = null
            listener.onMotionMoveCanceled(i, j)
            return
        }
        when {
            clickedTile != null -> {
                val move = MotionMove(clickedTile!!, tile)
                if (!ruleManager.checkMotionMoveForValidity(move)) return
                sendMotionMoveToModel(move)
                clickedTile = null
            }
            clickedReserve != null -> {
                val move = AddMove(clickedReserve!!, tile)
                if (!ruleManager.checkAddMoveForValidity(move)) return
                sendAddMoveToModel(move)
                clickedReserve = null
            }
            else -> {
                // if both clickedTile and clickedReserve == null we need to set clickedTile
                if (tile.isEmpty || tile.division!!.playerName == model.enemy.name) return
                clickedTile = tile
            }
        }
    }

    open fun processReserveClick(type: Division.Type, playerName: String) {
        val reserve = model.getPlayerByName(playerName).divisionResources.reserves[type]!!
        if (reserve == clickedReserve) {
            clickedReserve = null
            listener.onAddMoveCanceled()
            return
        }

    }

    fun sendMotionMoveToModel(motionMove: MotionMove) {
        model.handleMotionMove(motionMove)
        listener.onMotionMoveComplete(motionMove)
        ruleManager.nextTurn()
        if (ruleManager.meLost()) listener.onGameEnd(meWon = false)
        if (ruleManager.enemyLost()) listener.onGameEnd(meWon = true)
    }

    fun sendAddMoveToModel(addMove: AddMove) {
        model.handleAddMove(addMove)
        listener.onAddMoveComplete(addMove)
        ruleManager.nextTurn()
        if (ruleManager.meLost()) listener.onGameEnd(meWon = false)
        if (ruleManager.enemyLost()) listener.onGameEnd(meWon = true)
    }

    fun sendMoveToModel(move: Move) {
        if (move.type == Move.Type.MOTION) sendMotionMoveToModel(move as MotionMove)
        else sendAddMoveToModel(move as AddMove)
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