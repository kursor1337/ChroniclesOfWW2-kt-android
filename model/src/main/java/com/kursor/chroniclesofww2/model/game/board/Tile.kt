package com.kursor.chroniclesofww2.model.game.board

class Tile(val row: Int, val column: Int) {

    val coordinate = row * 10 + column

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