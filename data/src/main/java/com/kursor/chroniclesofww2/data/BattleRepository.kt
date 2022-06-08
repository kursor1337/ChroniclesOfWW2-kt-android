package com.kursor.chroniclesofww2.data

import android.content.Context

class BattleRepository(val context: Context) {

    val sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    val standardBattles: List<Battle>
    val customBattles: MutableList<Battle>

    fun saveCustomBattle() {
        TODO()
    }

    companion object {
        private const val PREF = "battle_pref"
    }

}