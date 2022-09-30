package com.kursor.chroniclesofww2.domain.tools

import com.kursor.chroniclesofww2.model.serializable.Battle
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object Moshi {

    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val BATTLE_DATA_ADAPTER: JsonAdapter<Battle.Data> = moshi.adapter(Battle.Data::class.java)

}