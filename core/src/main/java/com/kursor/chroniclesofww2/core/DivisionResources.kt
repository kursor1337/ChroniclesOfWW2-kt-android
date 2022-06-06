package com.kursor.chroniclesofww2.core

import com.kursor.chroniclesofww2.core.board.Division


class DivisionResources(
    resMap: Map<Division.Type, Int>,
    pName: String
) {


    var playerName = pName
        set(value) {
            reserves.forEach { (type, reserve) -> reserve.playerName = value }
            field = value
        }

    val reserves =
        resMap.map { (type, quantity) -> type to Reserve(type, quantity, playerName) }.toMap()

    val divisionCount: Int
        get() = reserves.values.sumOf { reserve -> reserve.size }

}

class Reserve(
    val type: Division.Type,
    var size: Int,
    var playerName: String
) {

    val isEmpty: Boolean
        get() = size == 0

    var listener: Listener? = null

    fun getNewDivision(): Division? {
        if (!isEmpty) {
            size--
            listener?.onGetNewDivision()
            return Division.newInstance(type, playerName)
        }
        return null
    }

    fun cancel() {
        size++
        listener?.onCancel()
    }

    interface Listener {
        fun onGetNewDivision()
        fun onCancel()
    }
}



