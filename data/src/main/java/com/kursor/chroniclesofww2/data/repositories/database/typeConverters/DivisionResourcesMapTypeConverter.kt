package com.kursor.chroniclesofww2.data.repositories.database.typeConverters

import androidx.room.TypeConverter
import com.kursor.chroniclesofww2.model.game.board.Division
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types

class DivisionResourcesMapTypeConverter {

    val type = Types.newParameterizedType(
        Map::class.java,
        Division.Type::class.java,
        Int::class.javaObjectType
    )

    val adapter: JsonAdapter<Map<Division.Type, Int>> = moshi.adapter(type)

    @TypeConverter
    fun fromMapToJson(map: Map<Division.Type, Int>): String = adapter.toJson(map)

    @TypeConverter
    fun fromJsonToMap(string: String): Map<Division.Type, Int> = adapter.fromJson(string)!!

}