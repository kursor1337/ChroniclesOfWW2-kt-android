package com.kursor.chroniclesofww2.core.board

import com.kursor.chroniclesofww2.core.Player
import com.kursor.chroniclesofww2.core.moves.MotionMove


class Board(val height: Int, val width: Int) {

    constructor(size: Int) : this(size, size)

    constructor() : this(DEFAULT_SIZE)


    private val tiles = List(height) { row ->
        List(width) { column -> Tile(row, column) }
    }

    operator fun get(i: Int, j: Int) = tiles[i][j]

    operator fun set(i: Int, j: Int, division: Division) {
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

    fun calculatePossibleMotionMoves(i: Int, j: Int, playerName: String): List<MotionMove> {
        if (tiles[i][j].isEmpty) return emptyList()
        val division = tiles[i][j].division!!
        val result = mutableListOf<MotionMove>()
        forEachTile { tile ->
            val move = MotionMove(tiles[i][j], tile)
            if (!division.isValidMove(move)) return@forEachTile
            if (tile.isEmpty || tile.division!!.playerName != playerName)
                result.add(move)
        }
        return result
    }

    fun calculatePossibleAddMoves(playerName: String)

    fun safeLine(enemyName: String, i: Int): Boolean {
        tiles[i].forEach { tile ->
            if (!tile.isEmpty && tile.division!!.playerName == enemyName) return false
        }
        return true
    }

    inline fun forEachTileIndexed(action: (Int, Int, Tile) -> Unit) {
        for (i in 0 until height) {
            for (j in 0 until width) {
                action(i, j, this[i, j])
            }
        }
    }

    inline fun forEachTile(action: (Tile) -> Unit) {
        forEachTileIndexed { i, j, value -> action(value) }
    }


    companion object {
        const val DEFAULT_SIZE = 8
    }


}