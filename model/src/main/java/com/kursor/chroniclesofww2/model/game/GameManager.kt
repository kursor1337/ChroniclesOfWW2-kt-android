package com.kursor.chroniclesofww2.model.game

import com.kursor.chroniclesofww2.model.Controller
import com.kursor.chroniclesofww2.model.data.GameData

object GameManager {

    fun initGame(gameData: GameData, listener: Controller.Listener): Controller {
        return Controller(Model(gameData), listener)
    }

}