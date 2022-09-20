package com.kursor.chroniclesofww2.data.repositories.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kursor.chroniclesofww2.data.repositories.database.daos.BattleDao
import com.kursor.chroniclesofww2.data.repositories.database.entitiies.BattleEntity
import com.kursor.chroniclesofww2.data.repositories.database.typeConverters.DivisionResourcesMapTypeConverter
import com.kursor.chroniclesofww2.data.repositories.database.typeConverters.NationTypeConverter

@Database(entities = [BattleEntity::class], version = 1)
@TypeConverters(
    DivisionResourcesMapTypeConverter::class,
    NationTypeConverter::class
)
abstract class Database : RoomDatabase() {

    abstract fun battleDao(): BattleDao

}