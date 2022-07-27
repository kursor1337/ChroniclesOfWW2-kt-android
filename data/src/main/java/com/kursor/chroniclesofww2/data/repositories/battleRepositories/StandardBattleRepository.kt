package com.kursor.chroniclesofww2.data.repositories.battleRepositories

import android.content.Context
import android.util.Log
import com.kursor.chroniclesofww2.data.R
import com.kursor.chroniclesofww2.model.data.Battle
import com.kursor.chroniclesofww2.model.game.Nation
import com.kursor.chroniclesofww2.model.game.board.Division

class StandardBattleRepository(context: Context) : BattleRepository {

    override val battleList: List<Battle> = initStandardScenarioList(context)

    override fun findBattleById(id: Int): Battle {
        return battleList[id]
    }

    private fun initStandardScenarioList(context: Context): List<Battle> {
        val nameArray = context.resources.getStringArray(R.array.standard_battle_names)
        val descriptionArray =
            context.resources.getStringArray(R.array.standard_battle_descriptions)
        val dataArray = getStandardScenarioDataList(context)
        val tempScenarioList = mutableListOf<Battle>()
        nameArray.forEachIndexed { index, name ->
            tempScenarioList += Battle(index, name, descriptionArray[index], dataArray[index])
        }
        return tempScenarioList
    }

    private fun getStandardScenarioDataList(context: Context): List<Battle.Data> {
        val dataStringArray = context.resources.getStringArray(R.array.standard_battle_data)
        val dataList = mutableListOf<Battle.Data>()
        dataStringArray.forEachIndexed { index, dataString ->
            Log.i("Repo", dataString)
            val lines = dataString.lines()
            var nation1: Nation? = null
            var nation2: Nation? = null
            val nation1divisions = mutableMapOf<Division.Type, Int>()
            val nation2divisions = mutableMapOf<Division.Type, Int>()
            var currentNation: Nation
            var currentNationDivisions = mutableMapOf<Division.Type, Int>()

            for (line in lines) {
                if (!line.contains(" to ")) {
                    currentNation = Nation.valueOf(line.trim())
                    if (nation1 == null) {
                        nation1 = currentNation
                        currentNationDivisions = nation1divisions
                    } else if (nation2 == null) {
                        nation2 = currentNation
                        currentNationDivisions = nation2divisions
                    }
                    continue
                }
                Log.i("Repo", line)
                val (typeString, quantityString) = line.trim().split(" to ")
                val type = Division.Type.valueOf(typeString)
                val quantity = quantityString.toInt()
                currentNationDivisions[type] = quantity
            }
            dataList.add(
                Battle.Data(
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