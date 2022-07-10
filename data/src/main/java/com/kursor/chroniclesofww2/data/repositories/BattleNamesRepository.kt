package com.kursor.chroniclesofww2.data.repositories

import com.kursor.chroniclesofww2.model.data.Battle

interface BattleNamesRepository {

    fun getLocalizedNameForBattle(battle: Battle): String

    fun getDefaultNameForBattle(battle: Battle): String

    fun getLocalizedNameById(id: Int): String

    fun getDefaultNameById(id: Int): String

}