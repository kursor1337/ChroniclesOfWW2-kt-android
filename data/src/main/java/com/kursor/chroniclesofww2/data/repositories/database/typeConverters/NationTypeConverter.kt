package com.kursor.chroniclesofww2.data.repositories.database.typeConverters

import androidx.room.TypeConverter
import com.kursor.chroniclesofww2.model.game.Nation

class NationTypeConverter {

    @TypeConverter
    fun fromNationToString(nation: Nation) = nation.toString()

    @TypeConverter
    fun fromStringToNation(string: String) = Nation.valueOf(string)

}