package com.kursor.chroniclesofww2.data.repositories.database.entitiies

import androidx.room.Entity
import com.kursor.chroniclesofww2.model.game.Nation
import com.kursor.chroniclesofww2.model.game.board.Division

@Entity
data class BattleDataEntity(
    val id: Int,
    val nation1: Nation,
    val nation1divisions: Map<Division.Type, Int>,
    val nation2: Nation,
    val nation2divisions: Map<Division.Type, Int>
)