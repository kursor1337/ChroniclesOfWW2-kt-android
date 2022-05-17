package com.kursor.chroniclesofww2.model.board

class Move(val start: Tile, val destination: Tile) {

    val isAttack: Boolean
        get() = !destination.isEmpty

}
