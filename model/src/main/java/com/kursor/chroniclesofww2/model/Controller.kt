package com.kursor.chroniclesofww2.model

import com.kursor.chroniclesofww2.model.game.Model
import com.kursor.chroniclesofww2.model.game.Reserve
import com.kursor.chroniclesofww2.model.game.RuleManager
import com.kursor.chroniclesofww2.model.game.board.Division
import com.kursor.chroniclesofww2.model.game.board.Tile
import com.kursor.chroniclesofww2.model.game.moves.AddMove
import com.kursor.chroniclesofww2.model.game.moves.MotionMove
import com.kursor.chroniclesofww2.model.game.moves.Move

class Controller(
    private val model: Model,
    private val listener: Listener
) {

    private val ruleManager = RuleManager(model)

    interface Listener {
        //on completed moves
        fun onMyMotionMoveComplete(motionMove: MotionMove)
        fun onMyAddMoveComplete(addMove: AddMove)
        fun onEnemyMoveComplete(move: Move)

        //on moves canceled
        fun onMyMotionMoveCanceled(i: Int, j: Int)
        fun onMyAddMoveCanceled()

        // on clicked
        fun onReserveClicked(reserve: Reserve)
        fun onTileClicked(tile: Tile)

        // on game events
        fun onGameEnd(meWon: Boolean)
        fun onStartingSecond()
    }

    private var clickedTile: Tile? = null
        set(value) {
            if (field == null && value != null) listener.onTileClicked(value)
            field = value
        }
    private var clickedReserve: Reserve? = null
        set(value) {
            if (field == null && value != null) listener.onReserveClicked(value)
            field = value
        }

    init {
        if (!model.me.isInitiator) listener.onStartingSecond()
    }

    fun processTileClick(i: Int, j: Int) {
        val tile = model.board[i, j]
        if (tile == clickedTile) {
            clickedTile = null
            listener.onMyMotionMoveCanceled(i, j)
            return
        }
        when {
            clickedTile != null -> {
                if (!ruleManager.isValidMotionMove(MotionMove(clickedTile!!, tile))) return
                val move = MotionMove(clickedTile!!, tile)
                model.handleMyMove(move)
                listener.onMyMotionMoveComplete(move)
            }
            clickedReserve != null -> {
                if (!ruleManager.isValidAddMove(AddMove(clickedReserve!!, tile))) return
                val move = AddMove(clickedReserve!!, tile)
                model.handleMyMove(move)
                listener.onMyAddMoveComplete(move)
            }
            else -> {
                // if both clickedTile and clickedReserve == null we need to set clickedTile
                if (tile.isEmpty || tile.division!!.playerName == model.enemy.name) return
                clickedTile = tile
            }
        }
    }

    fun processReserveClick(type: Division.Type) {
        val reserve = model.me.divisionResources.reserves[type]!!
        if (reserve == clickedReserve) {
            clickedReserve = null
            listener.onMyAddMoveCanceled()
        } else clickedReserve = reserve
    }

    fun processEnemyMove(move: Move) {
        model.handleEnemyMove(move)
        listener.onEnemyMoveComplete(move)
    }

}