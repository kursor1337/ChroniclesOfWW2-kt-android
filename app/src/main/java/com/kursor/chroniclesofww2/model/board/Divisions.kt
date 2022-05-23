package com.kursor.chroniclesofww2.model.board

import com.kursor.chroniclesofww2.R
import kotlin.math.abs

abstract class Division(val type: Type, val playerName: String) {

    enum class Type {
        INFANTRY, ARMORED, ARTILLERY
    }

    abstract val MAX_HP: Int
    abstract var hp: Int
    abstract val softAttack: Int
    abstract val hardAttack: Int
    val isDead: Boolean
        get() = hp <= 0

    abstract fun isValidMove(move: Move): Boolean

    fun moveOrAttack(move: Move) {
        if (move.isAttack) attack(move.destination)
        else move(move)
    }

    private fun move(move: Move) {
        move.destination.division = this
        move.start.division = null
    }

    private fun attack(tile: Tile) {
        val division = tile.division!!
        if (division.type == Type.INFANTRY) division.takeDamage(softAttack)
        else division.takeDamage(hardAttack)
        if (division.isDead) tile.division = null
    }

    private fun takeDamage(damage: Int) {
        hp -= damage
    }

    companion object {
        fun newInstance(type: Type, playerName: String): Division {
            return when (type) {
                Type.INFANTRY -> InfantryDivision(playerName)
                Type.ARMORED -> ArmoredDivision(playerName)
                Type.ARTILLERY -> ArtilleryDivision(playerName)
            }
        }

        fun getDrawableResource(type: Type) = when (type) {
            Type.INFANTRY -> R.drawable.unit_infantry
            Type.ARMORED -> R.drawable.unit_armored
            Type.ARTILLERY -> R.drawable.unit_artillery
        }
    }
}

class InfantryDivision(playerName: String) : Division(Type.INFANTRY, playerName) {
    override val MAX_HP = 250
    override var hp = MAX_HP
    override val softAttack = 125
    override val hardAttack = 20

    override fun isValidMove(move: Move): Boolean {
        return abs(move.start.row - move.destination.row) < 2 &&
                abs(move.start.column - move.destination.column) < 2
    }
}

class ArmoredDivision(playerName: String) : Division(Type.ARMORED, playerName) {
    override val MAX_HP = 200
    override var hp = MAX_HP
    override val softAttack = 75
    override val hardAttack = 100

    override fun isValidMove(move: Move): Boolean {
        return abs(move.start.row - move.destination.row) < 3 &&
                abs(move.start.column - move.destination.column) < 3
    }
}

class ArtilleryDivision(playerName: String) : Division(Type.ARTILLERY, playerName) {
    override val MAX_HP = 100
    override var hp = MAX_HP
    override val softAttack = 75
    override val hardAttack = 100

    override fun isValidMove(move: Move): Boolean {
        return abs(move.start.row - move.destination.row) +
                abs(move.start.column - move.destination.column) < 2
    }
}
