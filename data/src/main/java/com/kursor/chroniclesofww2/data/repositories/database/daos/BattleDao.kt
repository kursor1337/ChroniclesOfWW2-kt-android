package com.kursor.chroniclesofww2.data.repositories.database.daos

import androidx.room.*
import com.kursor.chroniclesofww2.data.repositories.database.entitiies.BattleEntity

@Dao
interface BattleDao {


    @Query("SELECT * FROM BattleEntity")
    fun getAll(): List<BattleEntity>

    @Query("SELECT * FROM BattleEntity WHERE id LIKE :id LIMIT 1")
    fun get(id: Int): BattleEntity?

    @Insert
    fun insert(battleEntity: BattleEntity)

    @Update
    fun update(battleEntity: BattleEntity)

    @Delete
    fun delete(battleEntity: BattleEntity)

}