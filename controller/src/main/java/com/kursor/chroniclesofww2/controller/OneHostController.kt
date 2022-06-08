package com.kursor.chroniclesofww2.controller

import com.kursor.chroniclesofww2.model.game.Model
import com.kursor.chroniclesofww2.model.game.board.Division
import com.kursor.chroniclesofww2.model.game.moves.AddMove
import com.kursor.chroniclesofww2.model.game.moves.MotionMove

class OneHostController(
    model: Model,
    listener: Listener
) : Controller(model, listener)