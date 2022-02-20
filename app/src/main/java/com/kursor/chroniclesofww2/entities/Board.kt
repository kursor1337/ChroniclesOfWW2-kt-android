package com.kursor.chroniclesofww2.entities

const val DEFAULT_BOARD_SIZE = 8

class Board(val size: Int) {

    val tiles = List(size) { row ->
        List(size) { column -> Tile(row, column) }
    }

}