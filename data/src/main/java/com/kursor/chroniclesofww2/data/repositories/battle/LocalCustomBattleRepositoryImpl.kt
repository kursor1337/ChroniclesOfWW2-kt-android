package com.kursor.chroniclesofww2.data.repositories.battle

import android.content.Context
import com.kursor.chroniclesofww2.data.repositories.database.daos.BattleDao
import com.kursor.chroniclesofww2.data.repositories.database.entitiies.BattleDataEntity
import com.kursor.chroniclesofww2.data.repositories.database.entitiies.BattleEntity
import com.kursor.chroniclesofww2.domain.repositories.LocalCustomBattleRepository
import com.kursor.chroniclesofww2.model.serializable.Battle
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking

class LocalCustomBattleRepositoryImpl(
    val context: Context,
    val battleDao: BattleDao
) : LocalCustomBattleRepository {

    override val PREFIX = -1_000_000_000

    override val battleList: List<Battle>
        get() = runBlocking { getAllBattles() }

    private val sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    private var nextBattleId = sharedPreferences.getInt(NEXT_BATTLE_ID, PREFIX)
        set(value) {
            field = value
            sharedPreferences.edit().putInt(NEXT_BATTLE_ID, value).apply()
        }

    override fun findBattleById(id: Int): Battle? {
        return runBlocking {
            battleDao.get(id)
        }
    }

    override fun nextBattleId(): Int {
        return nextBattleId
    }

    override suspend fun getAllBattles(): List<Battle> =
        battleDao.getAll().map { it.toModelEntity() }

    override suspend fun saveBattle(battle: Battle) {
        battleDao.insert(battle.toDatabaseEntity())
    }

    override suspend fun editBattle(battle: Battle) {
        battleDao.update(battle.toDatabaseEntity())
    }

    override suspend fun deleteBattle(battle: Battle) {
        battleDao.delete(battle.toDatabaseEntity())
    }

    override suspend fun deleteBattle(id: Int) {
        battleDao.delete(findBattleById(id)?.toDatabaseEntity() ?: return)
    }

    private fun Battle.toDatabaseEntity(): BattleEntity = BattleEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        data = this.data.toDatabaseEntity()
    )

    private fun BattleEntity.toModelEntity(): Battle = Battle(
        id = this.id,
        name = this.name,
        description = this.description,
        data = this.data.toModelEntity()
    )

    private fun Battle.Data.toDatabaseEntity(): BattleDataEntity = BattleDataEntity(
        id = this.id,
        nation1 = this.nation1,
        nation1divisions = this.nation1divisions,
        nation2 = this.nation2,
        nation2divisions = this.nation2divisions
    )

    private fun BattleDataEntity.toModelEntity(): Battle.Data = Battle.Data(
        id = this.id,
        nation1 = this.nation1,
        nation1divisions = this.nation1divisions,
        nation2 = this.nation2,
        nation2divisions = this.nation2divisions
    )

    companion object {
        private const val PREF = "pref"
        private const val KEY = "CustomBattleRepository_key"
        private const val NEXT_BATTLE_ID = "next battle id"
    }
}