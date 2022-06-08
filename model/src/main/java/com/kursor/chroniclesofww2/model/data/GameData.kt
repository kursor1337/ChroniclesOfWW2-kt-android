package com.kursor.chroniclesofww2.model.data

import com.kursor.chroniclesofww2.model.game.DivisionResources

data class GameData(
    private val myName: String,
    private val enemyName: String,
    private val battleData: Battle.Data,
    val boardHeight: Int,
    val boardWidth: Int,
    val invertNations: Boolean = false,
    val meInitiator: Boolean = true
) {

    val me = if (invertNations) Player(
        myName,
        DivisionResources(battleData.nation2divisions, myName),
        battleData.nation2,
        isInitiator = meInitiator
    ) else Player(
        myName,
        DivisionResources(battleData.nation1divisions, myName),
        battleData.nation1,
        isInitiator = meInitiator
    )

    val enemy = if (invertNations) Player(
        enemyName,
        DivisionResources(battleData.nation1divisions, enemyName),
        battleData.nation1,
        isInitiator = !meInitiator
    ) else Player(
        enemyName,
        DivisionResources(battleData.nation2divisions, enemyName),
        battleData.nation2,
        isInitiator = !meInitiator
    )

    fun getVersionForAnotherPlayer(): GameData {
        return GameData(
            enemyName,
            myName,
            battleData,
            boardHeight,
            boardWidth,
            invertNations = !invertNations,
            meInitiator = !meInitiator
        )
    }

}