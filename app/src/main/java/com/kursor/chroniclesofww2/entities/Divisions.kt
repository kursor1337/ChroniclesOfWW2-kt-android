package com.kursor.chroniclesofww2.entities

import kotlin.math.abs

abstract class Division(val id: Int, val type: Type) {

    abstract val MAX_HP: Int
    abstract var hp: Int
    abstract val softAttack: Int
    abstract val hardAttack: Int
    val isDead: Boolean
        get() = hp <= 0

    abstract fun isValidMove(move: Move): Boolean

    enum class Type {
        INFANTRY, ARMORED, ARTILLERY
    }
}

class InfantryDivision(id: Int) : Division(id, Type.INFANTRY) {
    override val MAX_HP = 250
    override var hp = MAX_HP
    override val softAttack = 125
    override val hardAttack = 20

    override fun isValidMove(move: Move): Boolean {
        return abs(move.start.row - move.destination.row) < 2 &&
                abs(move.start.column - move.destination.column) < 2
    }
}

class ArmoredDivision(id: Int) : Division(id, Type.ARMORED) {
    override val MAX_HP = 200
    override var hp = MAX_HP
    override val softAttack = 75
    override val hardAttack = 100

    override fun isValidMove(move: Move): Boolean {
        return abs(move.start.row - move.destination.row) < 3 &&
                abs(move.start.column - move.destination.column) < 3
    }
}

class ArtilleryDivision(id: Int) : Division(id, Type.ARTILLERY) {
    override val MAX_HP = 100
    override var hp = MAX_HP
    override val softAttack = 75
    override val hardAttack = 100

    override fun isValidMove(move: Move): Boolean {
        return abs(move.start.row - move.destination.row) +
                abs(move.start.column - move.destination.column) < 2
    }
}