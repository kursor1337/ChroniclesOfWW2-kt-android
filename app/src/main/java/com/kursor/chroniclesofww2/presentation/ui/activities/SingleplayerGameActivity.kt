package com.kursor.chroniclesofww2.presentation.ui.activities

import com.kursor.chroniclesofww2.model.controllers.Controller
import com.kursor.chroniclesofww2.model.controllers.OneHostController
import com.kursor.chroniclesofww2.model.data.GameData
import com.kursor.chroniclesofww2.model.game.Model
import com.kursor.chroniclesofww2.model.game.moves.Move

class SingleplayerGameActivity : GameActivity() {
    override fun initController(gameData: GameData, listener: Controller.Listener) {
        controller = OneHostController(Model(gameData), listener)
    }

    override fun notifyEnemy(move: Move) {
        TODO("Not yet implemented")
    }
}