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
    lateinit var customScenarioList: List<Scenario>
//    lateinit var standardScenarioDataList: List<Scenario.Data>
//    lateinit var standardScenarioNameList: List<String>
//    lateinit var standardScenarioDescriptionList: List<String>
//    lateinit var customScenarioDataList: MutableList<Scenario.Data>
//    lateinit var customScenarioNameList: MutableList<String>
//    lateinit var customScenarioDescriptionList: MutableList<String>

    fun init(context: Context) {
        initStandardScenarioNames(context)
        getStandardScenarioDataList(context)
    }

    fun initStandardScenarios(context: Context) {
        val nameArray = context.resources.getStringArray(R.array.scenario_names)
        val descriptionArray = context.resources.getStringArray(R.array.scenario_descriptions)
        val dataArray = getStandardScenarioDataList(context)
        val tempScenarioList = mutableListOf<Scenario>()
        nameArray.forEachIndexed { index, name ->
            tempScenarioList += Scenario(index, name, descriptionArray[index], dataArray[index])
        }
        standardScenarioList = tempScenarioList
    }

    fun initCustomScenarios() {
        customScenarioList = Tools.GSON.fromJson(
            Tools.getString(CUSTOM_SCENARIOS),
            object : TypeToken<List<Scenario>>() {}.type
        )
    }

    private fun initStandardScenarioNames(context: Context) {
        standardScenarioNameList =
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

    fun saveCustomScenario(scenario: Scenario) {
        customScenarioDataList += scenario.data
        customScenarioDescriptionList += scenario.localizedDescription
        customScenarioNameList += scenario.localizedName

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

    fun findStandardScenarioById(id: Int) = Scenario(
        id,
        standardScenarioNameList[id],
        standardScenarioDescriptionList[id],
        standardScenarioDataList[id]
    )

    fun findCustomScenarioById(id: Int): Scenario {
        val customId = id - CUSTOM_PREFIX
        return Scenario(
            customId,
            customScenarioNameList[customId],
            customScenarioDescriptionList[customId],
            customScenarioDataList[customId]
        )
    }
}