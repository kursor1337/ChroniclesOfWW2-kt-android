package com.kursor.chroniclesofww2.core.board

import com.kursor.chroniclesofww2.core.moves.MotionMove

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

    fun isValidMove(motionMove: MotionMove) = isValidMotion(motionMove) || isValidAttack(motionMove)

    protected abstract fun isValidAttack(motionMove: MotionMove): Boolean

    protected abstract fun isValidMotion(motionMove: MotionMove): Boolean

    fun moveOrAttack(motionMove: MotionMove) {
        if (motionMove.isAttack) attack(motionMove.destination)
        else move(motionMove)
    }

    private fun move(motionMove: MotionMove) {
        motionMove.destination.division = this
        motionMove.start.division = null
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
    }
}

class InfantryDivision(playerName: String) : Division(Type.INFANTRY, playerName) {
    override val MAX_HP = 250
    override var hp = MAX_HP
    override val softAttack = 125
    override val hardAttack = 20

    override fun isValidAttack(motionMove: MotionMove): Boolean {
        return motionMove.isAttack &&
                motionMove.destination.division?.playerName != this.playerName &&
                motionMove.verticalDistance < 2 &&
                motionMove.horizontalDistance < 2
    }

    override fun isValidMotion(motionMove: MotionMove): Boolean {
        return motionMove.destination.isEmpty &&
                motionMove.verticalDistance < 2 &&
                motionMove.horizontalDistance < 2
    }
}

class ArmoredDivision(playerName: String) : Division(Type.ARMORED, playerName) {
    override val MAX_HP = 200
    override var hp = MAX_HP
    override val softAttack = 75
    override val hardAttack = 100

    override fun isValidAttack(motionMove: MotionMove): Boolean {
        return motionMove.isAttack &&
                motionMove.destination.division?.playerName != this.playerName &&
                motionMove.distance < 2
    }

    override fun isValidMotion(motionMove: MotionMove): Boolean {
        return motionMove.destination.isEmpty &&
                motionMove.distance < 3
    }
}

class ArtilleryDivision(playerName: String) : Division(Type.ARTILLERY, playerName) {
    override val MAX_HP = 100
    override var hp = MAX_HP
    override val softAttack = 75
    override val hardAttack = 100

    override fun isValidAttack(motionMove: MotionMove): Boolean {
        return motionMove.isAttack &&
                motionMove.destination.division?.playerName != this.playerName &&
                motionMove.distance < 4
    }

    override fun isValidMotion(motionMove: MotionMove): Boolean {
        return motionMove.destination.isEmpty &&
                motionMove.distance < 2
    }
}
