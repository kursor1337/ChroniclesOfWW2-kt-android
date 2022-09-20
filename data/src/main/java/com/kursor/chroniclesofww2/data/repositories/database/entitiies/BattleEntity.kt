package com.kursor.chroniclesofww2.data.repositories.database.entitiies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kursor.chroniclesofww2.model.serializable.Battle

@Entity
data class BattleEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val data: BattleDataEntity
)