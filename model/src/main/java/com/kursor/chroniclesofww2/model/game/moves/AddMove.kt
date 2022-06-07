package com.kursor.chroniclesofww2.model.game.moves

import com.kursor.chroniclesofww2.model.game.Reserve
import com.kursor.chroniclesofww2.model.game.board.Division
import com.kursor.chroniclesofww2.model.game.board.Tile

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
    }

}