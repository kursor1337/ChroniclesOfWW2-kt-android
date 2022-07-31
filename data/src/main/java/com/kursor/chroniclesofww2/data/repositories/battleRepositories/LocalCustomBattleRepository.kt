package com.kursor.chroniclesofww2.data.repositories.battleRepositories

import android.content.Context
import android.content.SharedPreferences
import com.kursor.chroniclesofww2.model.data.Battle
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class LocalCustomBattleRepository(
    context: Context
) : MutableBattleRepository {


    override val PREFIX = 1_000_000_000

    private val moshi =
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<MutableList<Battle>>(
            Types.newParameterizedType(MutableList::class.java, Battle::class.java)
        )

    private val sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
    private val _battleList: MutableList<Battle> = initCustomBattles()
    override val battleList: List<Battle>
        get() = _battleList
    var nextBattleId = PREFIX + battleList.size
        private set


    override fun findBattleById(id: Int): Battle {
        val index = id - PREFIX
        return battleList[index]
    }

    override fun nextBattleId(): Int {
        return nextBattleId
    }

    override fun saveBattle(battle: Battle) {
        _battleList.add(Battle(nextBattleId(), battle.name, battle.description, battle.data))
        updateStorage()
        nextBattleId++
    }

    fun deleteBattle(battle: Battle) {
        _battleList.remove(battle)
        updateStorage()
        nextBattleId--
    }

    fun deleteBattle(id: Int) {
        val index = id - PREFIX
        _battleList.removeAt(index)
        updateStorage()
        nextBattleId--
    }

    fun updateStorage() {
        sharedPreferences.edit().putString(KEY, moshi.toJson(_battleList)).apply()
    }


    private fun initCustomBattles(): MutableList<Battle> {
        val listJson = sharedPreferences.getString(KEY, "")
        if (listJson !is String) return mutableListOf()
        if (listJson.isBlank()) return mutableListOf()
        return moshi.nonNull().fromJson(listJson)!!
    }

    companion object {
        private const val PREF = "pref"
        private const val KEY = "CustomBattleRepository_key"
    }
}