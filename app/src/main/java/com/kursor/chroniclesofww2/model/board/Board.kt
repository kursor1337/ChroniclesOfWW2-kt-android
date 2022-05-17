package com.kursor.chroniclesofww2.model.board

import com.kursor.chroniclesofww2.model.Player

const val DEFAULT_BOARD_SIZE = 8

class Board(val height: Int, val width: Int) {

    constructor(size: Int) : this(size, size)

    constructor() : this(DEFAULT_BOARD_SIZE)


    private val tiles = List(height) { row ->
        List(width) { column -> Tile(row, column) }
    }

    operator fun get(i: Int, j: Int) = tiles[i][j]

    fun placeDivision(division: Division, i: Int, j: Int) {
        tiles[i][j].division = division
    }

    fun getListOfDivisions(player: Player) = getListOfDivisions(player.name)

    fun getListOfDivisions(playerName: String): List<Division> {
        return mutableListOf<Division>().also {
            forEachTile { tile ->
                if (tile.division?.playerName == playerName) it.add(tile.division!!)
            }
        }
    }

    inline fun Board.forEachTileIndexed(action: (Int, Int, Tile) -> Unit) {
        for (i in 0 until height) {
            for (j in 0 until width) {
                action(i, j, this[i, j])
            }
        }
    }

    inline fun Board.forEachTile(action: (Tile) -> Unit) {
        forEachTileIndexed { i, j, value -> action(value) }
    }


}

class Tile(val row: Int, val column: Int) {

    var listener: Listener? = null

    val isEmpty: Boolean
        get() = division == null
    var division: Division? = null
        set(div) {
            if (div == null) listener?.onTileCleared()
            else listener?.onDivisionSet(div)
        }


    interface Listener {
        fun onDivisionSet(division: Division)
        fun onTileCleared()
    }

}