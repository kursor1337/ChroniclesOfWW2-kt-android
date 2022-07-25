package com.kursor.chroniclesofww2.data.repositories.battleRepositories

import android.content.Context
import com.kursor.chroniclesofww2.model.data.Battle
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class LocalCustomBattleRepository(
    context: Context
) : MutableBattleRepository {

    private val moshi =
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<MutableList<Battle>>(
            Types.newParameterizedType(MutableList::class.java, Battle::class.java)
        )

    val sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
    override val battleList: MutableList<Battle> = initCustomBattles()
    var nextBattleId = CUSTOM_PREFIX + battleList.size
        private set


    override fun findBattleById(id: Int): Battle {
        val index = id - CUSTOM_PREFIX
        return battleList[index]
    }

    override fun nextBattleId(): Int {
        return nextBattleId
    }

    override fun saveBattle(battle: Battle) {
        battleList.add(battle)
        updateStorage()
        nextBattleId++
    }

    fun deleteBattle(battle: Battle) {
        deleteBattle(battle.id)
    }

    fun deleteBattle(id: Int) {
        val index = id - CUSTOM_PREFIX
        battleList.removeAt(index)
        updateStorage()
        nextBattleId--
    }

    fun updateStorage() {
        sharedPreferences.edit().putString(KEY, moshi.toJson(battleList)).apply()
    }


    private fun initCustomBattles(): MutableList<Battle> {
        val listJson = sharedPreferences.getString(KEY, "")
        if (listJson !is String) return mutableListOf()
        if (listJson.isBlank()) return mutableListOf()
        return moshi.nonNull().fromJson(listJson)!!
    }

    companion object {
        private const val CUSTOM_PREFIX = 1_000_000_000
        private const val PREF = "pref"
        private const val KEY = "CustomBattleRepository_key"
    }
}