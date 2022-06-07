package com.kursor.chroniclesofww2.model.data

import com.kursor.chroniclesofww2.model.game.DivisionResources

data class GameData(
    private val player1name: String,
    private val player2name: String,
    private val battleData: Battle.Data,
    val boardHeight: Int,
    val boardWidth: Int
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
                    DivisionResources(battleData.nation2divisions, player1name),
                    battleData.nation2,
                    isInitiator = true
                )
                else Player(
                    player1name,
                    DivisionResources(battleData.nation1divisions, player1name),
                    battleData.nation1,
                    isInitiator = true
                )
            }
            return field
        }


    var a = 5
        private set

    private var player2: Player? = null
        get() {
            if (field == null) {
                field = if (invertNations) Player(
                    player2name,
                    DivisionResources(battleData.nation1divisions, player2name),
                    battleData.nation1,
                    isInitiator = false
                )
                else Player(
                    player2name,
                    DivisionResources(battleData.nation2divisions, player2name),
                    battleData.nation2,
                    isInitiator = false
                )
            }
            return field
        }

    fun updatePlayers() {
        player1 = if (invertNations) Player(
            player1name,
            DivisionResources(battleData.nation2divisions, player1name),
            battleData.nation2,
            isInitiator = true
        ) else Player(
            player1name,
            DivisionResources(battleData.nation1divisions, player1name),
            battleData.nation1,
            isInitiator = true
        )
        player2 = if (invertNations) Player(
            player2name,
            DivisionResources(battleData.nation1divisions, player2name),
            battleData.nation1,
            isInitiator = false
        ) else Player(
            player2name,
            DivisionResources(battleData.nation2divisions, player2name),
            battleData.nation2,
            isInitiator = false
        )
    }

    var invertNations = false

    fun getVersionForAnotherPlayer(): GameData {

    }

}