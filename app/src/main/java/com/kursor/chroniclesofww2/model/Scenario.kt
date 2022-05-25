package com.kursor.chroniclesofww2.model

import com.kursor.chroniclesofww2.Tools

class Scenario(
    val name: String,
    val intro: String,
    val nation1: Nation,
    val nation1divisionResources: DivisionResources,
    val nation2: Nation,
    val nation2divisionResources: DivisionResources
) {

    var isCompleted = false
        private set

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

        fun fromJson(string: String): Scenario = Tools.GSON.fromJson(string, Scenario::class.java)
    }
}