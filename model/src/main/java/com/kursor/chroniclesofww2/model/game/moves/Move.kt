package com.kursor.chroniclesofww2.model.game.moves

import com.kursor.chroniclesofww2.model.game.board.Tile

abstract class Move {

    abstract val destination: Tile

    enum class Type {
        ADD, MOTION
    }

    abstract val type: Type

    abstract fun encodeToString(): String

    companion object {

        fun decodeFromStringToSimplified(string: String): Simplified {
            val type = Type.valueOf(string.substringBefore("-"))
            return if (type == Type.ADD) {
                AddMove.decodeFromStringToSimplified(string)
            } else MotionMove.decodeFromStringToSimplified(string)
        }
    }

    abstract class Simplified {
        abstract val type: Type
        abstract val destinationCoordinate: Int
    }

}