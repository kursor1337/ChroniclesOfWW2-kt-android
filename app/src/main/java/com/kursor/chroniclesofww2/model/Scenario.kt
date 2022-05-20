package com.kursor.chroniclesofww2.model

import com.kursor.chroniclesofww2.Tools

class Scenario(
    val name: String,
    val intro: String,
    private val player1: Player,
    private val player2: Player
) {

    var isCompleted = false
        private set

    val me: Player
        get() = if (invertPlayers) player2
        else player1
    val enemy: Player
        get() = if (invertPlayers) player1
        else player2

    var invertPlayers = false

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