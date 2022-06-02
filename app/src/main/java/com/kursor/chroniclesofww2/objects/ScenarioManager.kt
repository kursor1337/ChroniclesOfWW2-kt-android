package com.kursor.chroniclesofww2.objects

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.model.Nation
import com.kursor.chroniclesofww2.model.Scenario
import com.kursor.chroniclesofww2.model.board.Division

object ScenarioManager {

    private const val CUSTOM_PREFIX = 1_000_000_000
    private const val CUSTOM_SCENARIOS = "custom scenarios"

    lateinit var standardScenarioList: List<Scenario>
    lateinit var customScenarioList: MutableList<Scenario>

    fun init(context: Context) {
        initStandardScenarios(context)
        initCustomScenarios()
    }


    fun saveCustomScenario(scenario: Scenario) {
        customScenarioList += scenario
        Tools.saveString(CUSTOM_SCENARIOS, Tools.GSON.toJson(customScenarioList))
    }

    fun defaultScenarioData() =
        Scenario.Data(
            Scenario.DEFAULT,
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

    fun findScenarioById(id: Int): Scenario {
        val customId = id - CUSTOM_PREFIX
        return if (customId < 0) {
            standardScenarioList[id]
        } else customScenarioList[customId]
    }


    private fun initStandardScenarios(context: Context) {
        val nameArray = context.resources.getStringArray(R.array.scenario_names)
        val descriptionArray = context.resources.getStringArray(R.array.scenario_descriptions)
        val dataArray = getStandardScenarioDataList(context)
        val tempScenarioList = mutableListOf<Scenario>()
        nameArray.forEachIndexed { index, name ->
            tempScenarioList += Scenario(index, name, descriptionArray[index], dataArray[index])
        }
        standardScenarioList = tempScenarioList
    }

    private fun initCustomScenarios() {
        val listJson = Tools.getString(CUSTOM_SCENARIOS)

        customScenarioList = if (listJson == "") mutableListOf()
        else Tools.GSON.fromJson(listJson, object : TypeToken<List<Scenario>>() {}.type)
    }

    private fun getStandardScenarioDataList(context: Context): MutableList<Scenario.Data> {
        val dataStringArray = context.resources.getStringArray(R.array.scenario_data)
        val dataList = mutableListOf<Scenario.Data>()
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
                Scenario.Data(
                    index,
                    nation1!!,
                    nation1divisions,
                    nation2!!,
                    nation2divisions
                )
            )
        }
        return dataList
    }
}