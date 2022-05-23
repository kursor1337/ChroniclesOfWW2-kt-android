package com.kursor.chroniclesofww2.model

import com.kursor.chroniclesofww2.Tools

class Scenario(
    val name: String,
    val intro: String,
    private val player1name: String,
    private val divisionResources1: DivisionResources,
    private val nation1: Nation,
    private val player2name: String,
    private val divisionResources2: DivisionResources,
    private val nation2: Nation
) {

    var isCompleted = false
        private set

    val me: Player
        get() = if (invertPlayers) player2
        else player1
    val enemy: Player
        get() = if (invertPlayers) player1
        else player2

    private val player1: Player
        get() = if (invertNations) Player(player1name, divisionResources2, nation2)
        else Player(player1name, divisionResources1, nation1)

    private val player2: Player
        get() = if (invertNations) Player(player2name, divisionResources1, nation1)
        else Player(player2name, divisionResources2, nation2)

    var invertPlayers = false

    var invertNations = false

    fun setCompleted() {
        isCompleted = true
    }

    override fun toString(): String {
        return name
    }

    companion object {
        @Transient
        const val DEFAULT = -865
        fun toJson(mission: Scenario): String = Tools.GSON.toJson(mission)

        fun fromJson(string: String?): Scenario = Tools.GSON.fromJson(string, Scenario::class.java)
    }
}