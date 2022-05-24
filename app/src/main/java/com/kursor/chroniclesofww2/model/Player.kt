package com.kursor.chroniclesofww2.model

import com.kursor.chroniclesofww2.model.board.MotionMove
import com.kursor.chroniclesofww2.model.board.Tile

class Player(
    val name: String,
    val divisionResources: DivisionResources,
    val nation: Nation,
    val turnMod: Int
) {

}