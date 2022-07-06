package com.kursor.chroniclesofww2

import com.kursor.chroniclesofww2.model.game.Nation
import com.kursor.chroniclesofww2.model.game.board.Division

fun Nation.getFlagResId() = when (this) {
    Nation.GERMANY -> R.drawable.nation_germany
    Nation.BRITAIN -> R.drawable.nation_britain
    Nation.FRANCE -> R.drawable.nation_france
    Nation.JAPAN -> R.drawable.nation_japan
    Nation.USSR -> R.drawable.nation_ussr
    Nation.USA -> R.drawable.nation_usa
    Nation.ITALY -> R.drawable.nation_italy
    Nation.DEFAULT -> R.drawable.nation_default
}

fun Division.getDivisionResId() = when (type) {
    Division.Type.INFANTRY -> R.drawable.unit_infantry
    Division.Type.ARMORED -> R.drawable.unit_armored
    Division.Type.ARTILLERY -> R.drawable.unit_artillery
}