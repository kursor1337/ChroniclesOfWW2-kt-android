package com.kursor.chroniclesofww2.data.repositories

import android.content.Context
import com.kursor.chroniclesofww2.model.data.Battle
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class CustomBattleRepository(
    context: Context
) : BattleRepository {


    private val moshi =
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<MutableList<Battle>>(
            Types.newParameterizedType(MutableList::class.java, Battle::class.java)
        )

    val sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
    override val battleList: MutableList<Battle> = initCustomScenarios()

    override fun findBattleById(id: Int): Battle {
        val customId = id - CUSTOM_PREFIX
        return battleList[customId]
    }

    fun saveBattle(battle: Battle) {
        battleList.add(battle)
        sharedPreferences.edit().putString(KEY, moshi.toJson(battleList)).apply()
    }

    private fun initCustomScenarios(): MutableList<Battle> {
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