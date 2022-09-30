package com.kursor.chroniclesofww2.data.repositories.database.entitiies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kursor.chroniclesofww2.model.game.Nation
import com.kursor.chroniclesofww2.model.game.board.Division

@Entity
data class BattleEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val nation1: Nation,
    @ColumnInfo val nation1divisions: Map<Division.Type, Int>,
    @ColumnInfo val nation2: Nation,
    @ColumnInfo val nation2divisions: Map<Division.Type, Int>
)