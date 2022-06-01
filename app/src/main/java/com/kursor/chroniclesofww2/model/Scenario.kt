package com.kursor.chroniclesofww2.model

import android.content.Context
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.model.board.Division

data class Scenario(
    val id: Int,
    val localizedName: String,
    val localizedDescription: String,
    val data: Data
) {

    fun getLocalizedName(context: Context) = if (id == DEFAULT) DEFAULT_MISSION_NAME
    else context.resources.getStringArray(R.array.scenario_names)[id]!!

    companion object {

        const val DEFAULT = -865

        const val DEFAULT_MISSION_NAME = "Default"

        fun toJson(mission: Scenario): String = Tools.GSON.toJson(mission)

        fun fromJson(string: String): Scenario = Tools.GSON.fromJson(string, Scenario::class.java)
    }

    data class Data(
        val id: Int,
        val nation1: Nation,
        val nation1divisions: Map<Division.Type, Int>,
        val nation2: Nation,
        val nation2divisions: Map<Division.Type, Int>
    ) {

    }
}