package com.kursor.chroniclesofww2.data.repositories.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kursor.chroniclesofww2.data.repositories.database.entitiies.BattleEntity
import com.kursor.chroniclesofww2.model.serializable.Battle

@Dao
interface BattleDao {


    @Query("SELECT * FROM BattleEntity")
    fun getAll(): List<BattleEntity>

    @Query("SELECT * FROM BattleEntity WHERE id LIKE :id LIMIT 1")
    fun get(id: Int): Battle?

    @Insert
    fun insert(battleEntity: BattleEntity)

    @Update
    fun update(battleEntity: BattleEntity)

    @Delete
    fun delete(battleEntity: BattleEntity)

}