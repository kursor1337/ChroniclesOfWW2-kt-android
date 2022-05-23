package com.kursor.chroniclesofww2.model.board

import com.kursor.chroniclesofww2.model.DivisionResources
import com.kursor.chroniclesofww2.model.Engine
import com.kursor.chroniclesofww2.model.Reserve

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

        abstract fun returnToFullState(engine: Engine): Move

    }

}

class AddMove(
    val divisionReserve: Reserve,
    val tile: Tile
) : Move() {

    override val type = Type.ADD

    override fun encodeToString() = "$type-${divisionReserve}:${tile.coordinate}"

    companion object {
        fun decodeFromStringToSimplified(string: String): Simplified {
            val divisionType =
                Division.Type.valueOf(string.substringAfter("-".substringBefore(":")))
            val tileCoordinate = string.substringAfter(":").toInt()
            return Simplified(divisionType, tileCoordinate)
        }
    }

    class Simplified(
        val divisionType: Division.Type,
        val tileCoordinate: Int
    ) : Move.Simplified() {

        override val type = Type.ADD

        override fun returnToFullState(engine: Engine): Move {
            return returnToFullState(engine.enemy.divisionResources, engine.board)
        }

        private fun returnToFullState(divisionResources: DivisionResources, board: Board): AddMove {
            return AddMove(
                divisionResources.resources[divisionType]!!,
                board[tileCoordinate / 10, tileCoordinate % 10]
            )
        }
    }

}

class MotionMove(
    val start: Tile,
    val destination: Tile
) : Move() {

    val isAttack: Boolean
        get() = !destination.isEmpty
    override val type = Type.MOTION

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

        override fun returnToFullState(engine: Engine): Move {
            return returnToFullState(engine.board)
        }

        private fun returnToFullState(board: Board): MotionMove {
            return MotionMove(
                board[startCoordinate / 10, startCoordinate % 10],
                board[destinationCoordinate / 10, destinationCoordinate % 10]
            )
        }


    }
}