package com.kursor.chroniclesofww2.core.moves

abstract class Move {

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
    }

}