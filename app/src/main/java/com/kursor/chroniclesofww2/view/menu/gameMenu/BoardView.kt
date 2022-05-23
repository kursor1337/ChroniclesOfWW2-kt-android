package com.kursor.chroniclesofww2.view.menu.gameMenu

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.Tools
import com.kursor.chroniclesofww2.model.board.Board
import com.kursor.chroniclesofww2.model.board.Division
import com.kursor.chroniclesofww2.model.board.MotionMove
import com.kursor.chroniclesofww2.model.board.Tile

class BoardView(
    context: Context,
    attributeSet: AttributeSet? = null
) : TableLayout(context, attributeSet) {

    var board: Board? = null
        set(value) {
            if (value != null) init(value)
            field = value
        }

    private lateinit var tileViews: List<List<TileView>>

    private fun init(board: Board) {
        tileViews = List(board.height) { i ->
            List(board.width) { j ->
                TileView(context).apply {
                    tile = board[i, j]
                    setOnClickListener {
                        val y = it.id / 10
                        val x = it.id % 10
                        val tile = board[y, x]
                        if (tile.isEmpty) return@setOnClickListener
                        showLegalMoves(
                            board.calculatePossibleMoves(
                                y, x, tile.division!!.playerName
                            )
                        )
                    }
                }
            }
        }

        val tileWidth = minOf(
            Tools.getScreenWidth() / board.width,
            Tools.getScreenHeight() / board.height
        )
        val tableRowLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, tileWidth)
        val tileViewLayoutParams = TableRow.LayoutParams(tileWidth, tileWidth)
        for (i in 0 until board.height) {
            val currentTableRow = TableRow(context).apply { layoutParams = tableRowLayoutParams }
            addView(currentTableRow)
            for (j in 0 until board.width) {
                currentTableRow.addView(tileViews[i][j].apply {
                    layoutParams = tileViewLayoutParams
                })
            }
        }
    }

    fun showLegalMoves(motionMoveList: List<MotionMove>) {
        motionMoveList.forEach { move ->
            val dest = move.destination
            val state = if (move.isAttack) TileView.State.ATTACKED
            else TileView.State.LEGAL
            tileViews[dest.row][dest.column].state = state
        }
    }
}

class TileView(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatImageView(context, attributeSet) {

    enum class State {
        NORMAL, ATTACKED, LEGAL
    }

    private var isFogged = true //TODO(set and get for drawing fog)

    var state: State = State.NORMAL
        set(value) {
            onStateChanged(field, value)
            field = value
        }

    private val layerMap = mutableMapOf<Int, Drawable>()
    private var layerDrawable = LayerDrawable(emptyArray())

    var tile: Tile? = null
        set(value) {
            if (value != null) init(value)
            field = value
        }

    private fun init(tile: Tile) {
        state = State.NORMAL
        tile.listener = object : Tile.Listener {
            override fun onDivisionSet(division: Division) {
                Log.i("TileView", "Callback from Tile")
                Log.i(
                    "TileView",
                    "drawable resource = " + Division.getDrawableResource(division.type)
                )
                addLayer(Division.getDrawableResource(division.type))
            }

            override fun onTileCleared() {
                Log.i("TileView", "Callback from Tile")
                clear()
            }

        }
        id = tile.coordinate
        scaleType = ScaleType.CENTER_INSIDE
        setImage(R.drawable.empty)
    }

    private fun onStateChanged(stateCurrent: State, stateNext: State) {
        when (stateCurrent) {
            State.ATTACKED -> hideIsAttacked()
            State.LEGAL -> hideIsLegal()
            State.NORMAL -> {
                //nothing special here
            }
        }
        when (stateNext) {
            State.ATTACKED -> showIsAttacked()
            State.LEGAL -> showIsLegal()
            State.NORMAL -> {
                //nothing special here
            }
        }
    }

    fun setImage(@DrawableRes id: Int) {
        val drawable = ContextCompat.getDrawable(context, id) ?: return
        Log.i("TileView", "Setting image... $drawable")
        layerMap.clear()
        layerMap[id] = drawable
        val ds = layerMap.values.toTypedArray()
        layerDrawable = LayerDrawable(ds)
        setImageResource(0)
        setImageDrawable(layerDrawable)
        Log.i(
            "TileView",
            "Set image " + layerDrawable + " length = " + layerDrawable.numberOfLayers
        )
    }

    fun clear() {
        setImage(R.drawable.empty)
    }

    private fun hideIsLegal() {
        Log.i("TileView", "Hide is legal")
        removeLayer(R.drawable.legal)
    }

    private fun hideIsAttacked() {
        Log.i("TileView", "Hide is legal")
        removeLayer(R.drawable.attacked)
    }

    private fun showIsLegal() {
        addLayer(R.drawable.legal)
        Log.i("TileView", "Show is legal")
    }

    private fun showIsAttacked() {
        addLayer(R.drawable.attacked)
        Log.i("TileView", "Show is attacked")
    }

    private fun addLayer(@DrawableRes id: Int) {
        val drawable = ContextCompat.getDrawable(context, id) ?: return
        Log.i("TileView", drawable.toString())
        layerMap[id] = drawable
        Log.i("TileView", "addLayer")
        val ds = layerMap.values.toTypedArray()
        layerDrawable = LayerDrawable(ds)
        Log.i(
            "TileView",
            "Set image " + layerDrawable + " length = " + layerDrawable.getNumberOfLayers()
        )
        setImageDrawable(layerDrawable)
    }

    private fun removeLayer(@DrawableRes id: Int) {
        val d = layerMap.remove(id) ?: return
        d.alpha = 0
        Log.i("TileView", d.toString() + "")
        val ds = layerMap.values.toTypedArray()
        layerDrawable = LayerDrawable(ds)
    }
}