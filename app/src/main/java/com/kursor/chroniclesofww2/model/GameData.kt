package com.kursor.chroniclesofww2.model

import com.kursor.chroniclesofww2.model.board.Board

data class GameData(
    private val player1name: String,
    private val player2name: String,
    private val scenario: Scenario,
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
                    scenario.nation2divisionResources,
                    scenario.nation2,
                    1
                )
                else Player(player1name, scenario.nation1divisionResources, scenario.nation1, 1)
            }
            return field
        }

    private var player2: Player? = null
        get() {
            if (field == null) {
                field = if (invertNations) Player(
                    player2name,
                    scenario.nation1divisionResources,
                    scenario.nation1,
                    1
                )
                else Player(player2name, scenario.nation2divisionResources, scenario.nation2, 1)
            }
            return field
        }

    fun updatePlayers() {
        player1 = if (invertNations) Player(
            player1name,
            scenario.nation2divisionResources,
            scenario.nation2,
            1
        ) else Player(player1name, scenario.nation1divisionResources, scenario.nation1, 1)
        player2 = if (invertNations) Player(
            player2name,
            scenario.nation1divisionResources,
            scenario.nation1,
            1
        ) else Player(player2name, scenario.nation2divisionResources, scenario.nation2, 1)
    }

    var invertNations = false

}