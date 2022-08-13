package com.kursor.chroniclesofww2.objects

import com.kursor.chroniclesofww2.model.serializable.Battle
import com.kursor.chroniclesofww2.model.serializable.GameData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object Moshi {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val BATTLE_ADAPTER = moshi.adapter(Battle::class.java)

    val GAMEDATA_ADAPTER = moshi.adapter(GameData::class.java)

}