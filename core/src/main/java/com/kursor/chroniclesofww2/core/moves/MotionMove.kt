package com.kursor.chroniclesofww2.core.moves

import com.kursor.chroniclesofww2.core.board.Tile
import kotlin.math.abs

class MotionMove(
    val start: Tile,
    val destination: Tile
) : Move() {

    val isAttack: Boolean
        get() = !destination.isEmpty
    override val type = Type.MOTION

    val verticalDistance = abs(start.row - destination.row)
    val horizontalDistance = abs(start.column - destination.column)
    val distance = verticalDistance + horizontalDistance

    override fun encodeToString() = "$type-${start.coordinate}:${destination.coordinate}"

    companion object {

        fun decodeFromStringToSimplified(string: String): Simplified {
            val startCoordinate = string.substringAfter("-".substringBefore(":")).toInt()
            val destinationCoordinate = string.substringAfter(":").toInt()
            return Simplified(startCoordinate, destinationCoordinate)
        }

    }

    class Simplified(
        val startCoordinate: Int,
        val destinationCoordinate: Int
    ) : Move.Simplified() {

        override val type = Type.MOTION
    }
}