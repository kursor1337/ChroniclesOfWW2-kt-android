package com.kursor.chroniclesofww2.presentation.ui.activities

import android.os.Bundle
import com.kursor.chroniclesofww2.model.controllers.Controller
import com.kursor.chroniclesofww2.model.controllers.OneHostController
import com.kursor.chroniclesofww2.model.data.GameData
import com.kursor.chroniclesofww2.model.game.DivisionResources
import com.kursor.chroniclesofww2.model.game.Model
import com.kursor.chroniclesofww2.model.game.board.Board
import com.kursor.chroniclesofww2.model.game.moves.Move
import com.kursor.chroniclesofww2.presentation.hudViews.ReserveView

class SinglePlayerGameActivity : GameActivity() {

    override fun initController(
        gameData: GameData,
        listener: Controller.Listener
    ): Pair<Model, Controller> {
        val model = Model(gameData)
        return model to OneHostController(model, listener)
    }

    override fun notifyEnemy(move: Move) {
        //TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        divisionResourcesEnemy.setOnReserveClickListener {
            controller.processReserveClick(
                it.reserve!!.type,
                divisionResourcesEnemy.divisionResources?.playerName
                    ?: return@setOnReserveClickListener
            )
        }
    }
}