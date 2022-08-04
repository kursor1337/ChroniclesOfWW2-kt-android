package com.kursor.chroniclesofww2.model.controllers

import com.kursor.chroniclesofww2.model.game.Model
import com.kursor.chroniclesofww2.model.game.board.Division

class OneHostController(
    model: Model,
    listener: Listener
) : Controller(model, listener) {
    override fun processReserveClick(type: Division.Type, playerName: String) {
        if (ruleManager.isMyTurn() && model.enemy.name == playerName) return
        if (ruleManager.isEnemyTurn() && model.me.name == playerName) return
        super.processReserveClick(type, playerName)
    }
}