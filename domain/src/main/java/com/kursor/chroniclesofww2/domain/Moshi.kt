package com.kursor.chroniclesofww2.domain

import com.kursor.chroniclesofww2.model.serializable.Battle
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object Moshi {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val BATTLE_DATA_ADAPTER = moshi.adapter(Battle.Data::class.java)

}