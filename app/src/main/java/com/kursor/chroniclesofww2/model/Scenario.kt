package com.kursor.chroniclesofww2.model

import android.content.Context
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.Tools

class Scenario(
    val name: String,
    val intro: String,
    val nation1: Nation,
    val nation1divisionResources: DivisionResources,
    val nation2: Nation,
    val nation2divisionResources: DivisionResources
) {

    override fun toString(): String {
        return name
    }

    companion object {

        const val DEFAULT = -865

        const val DEFAULT_MISSION_NAME = "Default"

        fun toJson(mission: Scenario): String = Tools.GSON.toJson(mission)

        fun fromJson(string: String): Scenario = Tools.GSON.fromJson(string, Scenario::class.java)

        fun defaultScenario(player1Name: String, player2Name: String) =
            Scenario(
                DEFAULT_MISSION_NAME, "",
                Nation.DEFAULT,
                DivisionResources.getDefaultInstance(player1Name),
                Nation.DEFAULT,
                DivisionResources.getDefaultInstance(player2Name)
            )

        fun getScenarioNames(context: Context): List<String> =
            context.resources.getStringArray(R.array.scenario_names).toList()

        fun getScenarioData(context: Context):  {

        }
    }
}