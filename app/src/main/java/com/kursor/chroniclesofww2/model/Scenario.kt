package com.kursor.chroniclesofww2.model

import android.content.Context
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.Tools
import com.kursor.chroniclesofww2.model.board.Division

class Scenario(
    val id: Int,
    val nation1: Nation,
    val nation1divisions: Map<Division.Type, Int>,
    val nation2: Nation,
    val nation2divisions: Map<Division.Type, Int>
) {

    fun getLocalizedName(context: Context) = if (id == DEFAULT) DEFAULT_MISSION_NAME
    else context.resources.getStringArray(R.array.scenario_names)[id]!!

    companion object {

        const val DEFAULT = -865

        const val DEFAULT_MISSION_NAME = "Default"

        fun toJson(mission: Scenario): String = Tools.GSON.toJson(mission)

        fun fromJson(string: String): Scenario = Tools.GSON.fromJson(string, Scenario::class.java)

        fun defaultScenario() =
            Scenario(
                DEFAULT,
                Nation.DEFAULT,
                mapOf(
                    Division.Type.INFANTRY to 9,
                    Division.Type.ARMORED to 3,
                    Division.Type.ARTILLERY to 3
                ),
                Nation.DEFAULT,
                mapOf(
                    Division.Type.INFANTRY to 9,
                    Division.Type.ARMORED to 3,
                    Division.Type.ARTILLERY to 3
                )
            )

        fun getScenarioNames(context: Context): List<String> =
            context.resources.getStringArray(R.array.scenario_names).toList()


        lateinit var scenarioDataList: List<Scenario>

        fun initScenarioList(context: Context) {
            val dataStringArray = context.resources.getStringArray(R.array.scenario_data)
            val dataList = mutableListOf<Scenario>()
            dataStringArray.forEachIndexed { index, dataString ->
                val lines = dataString.trimIndent().lines()
                var nation1: Nation? = null
                var nation2: Nation? = null
                val nation1divisions = mutableMapOf<Division.Type, Int>()
                val nation2divisions = mutableMapOf<Division.Type, Int>()
                var currentNation: Nation
                var currentNationDivisions = mutableMapOf<Division.Type, Int>()

                for (line in lines) {
                    if (!line.contains(" to ")) {
                        currentNation = Nation.valueOf(line)
                        if (nation1 == null) {
                            nation1 = currentNation
                            currentNationDivisions = nation1divisions
                        } else if (nation2 == null) {
                            nation2 = currentNation
                            currentNationDivisions = nation2divisions
                        }
                        continue
                    }
                    val (typeString, quantityString) = line.split(" to ")
                    val type = Division.Type.valueOf(typeString)
                    val quantity = quantityString.toInt()
                    currentNationDivisions[type] = quantity
                }
                dataList.add(
                    Scenario(
                        index,
                        nation1!!,
                        nation1divisions,
                        nation2!!,
                        nation2divisions
                    )
                )
            }
            scenarioDataList = dataList
        }


        fun getScenarioList(context: Context): List<Scenario> {
            if (!this::scenarioDataList.isInitialized) initScenarioList(context)
            return scenarioDataList
        }
    }
}