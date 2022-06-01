package com.kursor.chroniclesofww2.model

import com.kursor.chroniclesofww2.model.board.Board

data class GameData(
    private val player1name: String,
    private val player2name: String,
    private val scenarioData: Scenario.Data,
    val boardHeight: Int = Board.DEFAULT_SIZE,
    val boardWidth: Int = Board.DEFAULT_SIZE,
    val isFogEnabled: Boolean = false
) {

    val me: Player
        get() = player1!!
    val enemy: Player
        get() = player2!!

    private var player1: Player? = null
        get() {
            if (field == null) {
                field = if (invertNations) Player(
                    player1name,
                    DivisionResources(scenarioData.nation2divisions, player1name),
                    scenarioData.nation2,
                    1
                )
                else Player(
                    player1name,
                    DivisionResources(scenarioData.nation1divisions, player1name),
                    scenarioData.nation1,
                    1
                )
            }
            return field
        }

    private var player2: Player? = null
        get() {
            if (field == null) {
                field = if (invertNations) Player(
                    player2name,
                    DivisionResources(scenarioData.nation1divisions, player2name),
                    scenarioData.nation1,
                    1
                )
                else Player(
                    player2name,
                    DivisionResources(scenarioData.nation2divisions, player2name),
                    scenarioData.nation2,
                    1
                )
            }
            return field
        }

    fun updatePlayers() {
        player1 = if (invertNations) Player(
            player1name,
            DivisionResources(scenarioData.nation2divisions, player1name),
            scenarioData.nation2,
            1
        ) else Player(
            player1name,
            DivisionResources(scenarioData.nation1divisions, player1name),
            scenarioData.nation1,
            1
        )
        player2 = if (invertNations) Player(
            player2name,
            DivisionResources(scenarioData.nation1divisions, player2name),
            scenarioData.nation1,
            1
        ) else Player(
            player2name,
            DivisionResources(scenarioData.nation2divisions, player2name),
            scenarioData.nation2,
            1
        )
    }

    var invertNations = false

}